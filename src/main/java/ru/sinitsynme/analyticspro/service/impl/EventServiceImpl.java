package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.dto.filter.EventFilterDto;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.event.EventDateFilterType;
import ru.sinitsynme.analyticspro.entity.event.EventEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;
import ru.sinitsynme.analyticspro.exception.NoEventException;
import ru.sinitsynme.analyticspro.exception.ResourceNotFoundException;
import ru.sinitsynme.analyticspro.mapper.EventMapper;
import ru.sinitsynme.analyticspro.repository.ApplicationRepository;
import ru.sinitsynme.analyticspro.repository.EventRepository;
import ru.sinitsynme.analyticspro.repository.EventTypeRepository;
import ru.sinitsynme.analyticspro.service.EventService;
import ru.sinitsynme.analyticspro.utils.ListUtils;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ApplicationRepository applicationRepository;
    private final EventTypeRepository eventTypeRepository;

    private static final int HOUR_COUNT = 24;
    private static final int DAY_COUNT = 30;
    private static final int MONTH_COUNT = 12;
    private static final int YEAR_COUNT = 10;


    private static final long MILLIS_IN_HOUR = 3600 * 1000;
    private static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * HOUR_COUNT;
    private static final long MILLIS_IN_MONTH = MILLIS_IN_DAY * DAY_COUNT;
    private static final long MILLIS_IN_YEAR = MILLIS_IN_MONTH * MONTH_COUNT;

    private static final String HOUR_DATE_PATTERN = "HH";
    private static final String DAY_DATE_PATTERN = "dd.MM";
    private static final String MONTH_DATE_PATTERN = "MM.yy";
    private static final String YEAR_DATE_PATTERN = "yyyy";


    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, ApplicationRepository applicationRepository, EventTypeRepository eventTypeRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.applicationRepository = applicationRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public EventDto addEvent(EventDto eventDto) {
        if (eventDto.getName() == null || eventDto.getName().trim().isEmpty())
            throw new ResourceNotFoundException("Your event needs to have name");

        if (eventDto.getApplicationId() == null) {
            throw new ResourceNotFoundException("Your event needs to have applicationId");
        }

        Long appId = eventDto.getApplicationId();
        if (!applicationRepository.existsById(appId))
            throw new ResourceNotFoundException(String.format("Application with ID = %d was not found", appId));

        ApplicationEntity application = applicationRepository.getById(appId);

        EventEntity event = eventMapper.toEntity(eventDto);
        event.setName(event.getName().toUpperCase(Locale.ROOT));

        EventType eventType;

        Optional<EventType> optionalEventType = eventTypeRepository.findEventTypeByNameAndApplication(event.getName(), application);

        if (optionalEventType.isEmpty()) {
            eventType = new EventType(event.getName());
            eventType.setApplication(application);
        } else eventType = optionalEventType.get();


        event.setEventType(eventType);
        event.setApplication(application);
        event.setDate(Date.from(Instant.now()));

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public List<EventDto> addListOfEvents(List<EventDto> eventDtos) {
        for(EventDto eventDto: eventDtos){
            try {
                addEvent(eventDto);
            }
            catch (ResourceNotFoundException e){
                throw new ResourceNotFoundException(
                        String.format("Failed to save event: %s. Cause: %s. None of the events were persisted.", eventDto, e.getMessage()));
            }
        }
        return eventDtos;
    }

    @Override
    public List<EventDto> listAllApplicationEvents(Long applicationId) {
        if (!applicationRepository.existsById(applicationId))
            throw new ResourceNotFoundException(String.format("Application with ID = %d was not found", applicationId));

        ApplicationEntity application = applicationRepository.getById(applicationId);

        return eventRepository.findAllByApplication(application).stream()
                .map(eventMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> listApplicationEventsByFilter(EventFilterDto eventFilterDto) {
        Stream<EventEntity> stream = streamApplicationEventsByFilter(eventFilterDto);

        return stream.map(eventMapper::toDto).collect(Collectors.toList());
    }

    private Stream<EventEntity> streamApplicationEventsByFilter(EventFilterDto eventFilterDto) {
        Stream<EventEntity> stream = eventRepository.findAllByEventTypeIn(eventFilterDto.getTypeFilter())
                .stream();

        //Using a special filtering predicate depending on dateFilter
        if (eventFilterDto.getDateFilter() == EventDateFilterType.DAY) stream = stream.filter(isDayEvent());
        if (eventFilterDto.getDateFilter() == EventDateFilterType.MONTH) stream = stream.filter(isMonthEvent());
        if (eventFilterDto.getDateFilter() == EventDateFilterType.YEAR) stream = stream.filter(isYearEvent());
        return stream;
    }

    @Override
    public List<List<Object>> formEventPieDiagramData(EventFilterDto eventFilterDto) {
        //Filtering dtos
        List<EventDto> eventDtos = listApplicationEventsByFilter(eventFilterDto);

        //Grouping events by names and counting them
        Map<String, Long> chartData = eventDtos.stream().collect(Collectors.groupingBy(EventDto::getName, Collectors.counting()));

        //Mapping a map to a list of lists
        return ListUtils.mapToListOfPairs(chartData);
    }

    @Override
    public List<List<Object>> formEventLineDiagramData(EventFilterDto eventFilterDto) {
        //Filtering events and sorting down by date
        List<EventEntity> eventEntityList = streamApplicationEventsByFilter(eventFilterDto).sorted(EventEntity::compareTo).collect(Collectors.toList());

        if (eventEntityList.size() == 0) {
            throw new NoEventException(String.format("No events were found during query %s", eventFilterDto.getDateFilter().name()));
        }

        //Getting grouped events names
        List<String> eventNames = new ArrayList<>(eventEntityList.stream()
                .collect(Collectors.groupingBy(it -> it.getEventType().getName())).keySet());

        //Creating a map of eventNames indexes in result list (will be useful for performance later)
        Map<String, Integer> nameIndexes = new HashMap<>();
        for (int i = 0; i < eventNames.size(); i++) {
            nameIndexes.put(eventNames.get(i), i + 1);
        }

        Calendar calendar = Calendar.getInstance();

        //Getting current time to draw chart
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date startDate = calendar.getTime();

        //Creating a list for a line chart
        List<List<Object>> chartList = new ArrayList<>();
        chartList.add(new ArrayList<>());

        int timeCount;
        int calendarConst;
        long millisConst;
        int listWidth = eventNames.size() + 1;
        String datePattern;

        EventDateFilterType dateFilterType = eventFilterDto.getDateFilter();


        //Adapting algorithm of creating chart for specific date filter
        if (dateFilterType == EventDateFilterType.DAY) {
            chartList.get(0).add("Hour");
            timeCount = HOUR_COUNT;
            millisConst = MILLIS_IN_HOUR;
            calendarConst = Calendar.HOUR_OF_DAY;
            datePattern = HOUR_DATE_PATTERN;

        } else if (dateFilterType == EventDateFilterType.MONTH) {
            chartList.get(0).add("Day");
            timeCount = DAY_COUNT;
            millisConst = MILLIS_IN_DAY;
            calendarConst = Calendar.DAY_OF_MONTH;
            datePattern = DAY_DATE_PATTERN;

        } else if (dateFilterType == EventDateFilterType.YEAR) {
            chartList.get(0).add("Month");
            timeCount = MONTH_COUNT;
            millisConst = MILLIS_IN_MONTH;
            calendarConst = Calendar.MONTH;
            datePattern = MONTH_DATE_PATTERN;

        } else {
            chartList.get(0).add("Year");
            timeCount = YEAR_COUNT;
            millisConst = MILLIS_IN_YEAR;
            calendarConst = Calendar.YEAR;
            datePattern = YEAR_DATE_PATTERN;
        }

        chartList.get(0).addAll(eventNames);

        //Creating a map for keeping intermediate counting results and filling result chartList with 0
        Map<Integer, Map<String, Integer>> map = new LinkedHashMap<>();

        for (int i = 0; i < timeCount; i++) {
            calendar.setTimeInMillis(startDate.getTime() - i * millisConst);
            int timePiece = calendar.get(calendarConst);
            map.put(timePiece, new HashMap<>());

            chartList.add(new ArrayList<>(listWidth));
            for (int j = 0; j < listWidth; j++) {
                chartList.get(i + 1).add(0);
            }
            chartList.get(i + 1).set(0, timePiece);
        }

        //Filling map with event data
        for (EventEntity event : eventEntityList) {
            String eventName = event.getEventType().getName();
            calendar.setTime(event.getDate());
            int timePiece = calendar.get(calendarConst);
            Map<String, Integer> innerMap = map.get(timePiece);
            innerMap.put(eventName, innerMap.get(eventName) == null ? 1 : innerMap.get(eventName) + 1);
        }

        //Filling chartList with event data using map
        for (int key : map.keySet()) {
            int j = 1;
            for (Map<String, Integer> innerMap : map.values()) {
                for (String eventName : eventNames) {
                    int k = nameIndexes.get(eventName);
                    chartList.get(j).set(k, innerMap.get(eventName) == null ? 0 : innerMap.get(eventName));
                }
                j++;
            }
        }

        //Adapting dates in list for proper visibility
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

        for (int i = 1; i <= timeCount; i++) {
            calendar.setTimeInMillis(startDate.getTime() - (i - 1) * millisConst);
            chartList.get(i).set(0, simpleDateFormat.format(calendar.getTime()));
        }

        return chartList;
    }

    private static Predicate<EventEntity> isDayEvent() {
        return event -> event.getDate().after(new java.util.Date(System.currentTimeMillis() - MILLIS_IN_DAY));
    }

    private static Predicate<EventEntity> isMonthEvent() {
        return event -> event.getDate().after(new java.util.Date(System.currentTimeMillis() - MILLIS_IN_MONTH));
    }

    private static Predicate<EventEntity> isYearEvent() {
        return event -> event.getDate().after(new java.util.Date(System.currentTimeMillis() - MILLIS_IN_YEAR));
    }


}
