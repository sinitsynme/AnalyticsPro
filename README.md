# AnalyticsPro
Little project for VolgaIT'22.

**AnalyticsPro** is an analytic system designed for tracking events of registered applications. A single user is able to register their application in the system and get specific ID, which is used for tracking. Sending various application events  to the system by REST using this ID the user can see charts, displaying stats of their app.

# First steps for launching application

Unfortunately, Docker Compose is still a trouble for me*. 
I apologize, but to try my project you'll have to set application properties yourself...

You are supposed to: 
1) create a Postgres DB with name "analyticspro" at port 5432
2) set your current Postgres username and password.

*I truly tried to set this app on alpine, but something has been going wrong all the time.

# Main page
![image](https://user-images.githubusercontent.com/72615475/170872933-da73618a-af73-4980-967d-d8e547b2b999.png)

# Registration page

Enter your email and password

![image](https://user-images.githubusercontent.com/72615475/170873031-f06c845c-70b3-474a-b36b-7605140b6e2b.png)

# Main page after login
![image](https://user-images.githubusercontent.com/72615475/170873075-8ff8c2ca-9a7e-4b56-99e4-1782234d6491.png)

# Registering new application

Enter your unique application name

![image](https://user-images.githubusercontent.com/72615475/170873102-34fadac1-fdce-4e97-9c9f-b3d2a16096f6.png)

# Viewing list of your applications

Your applications get a unique "Application ID" with which you can get access to the app with API

![image](https://user-images.githubusercontent.com/72615475/170873143-de74854e-dca0-44de-94b4-64aeeb4fe892.png)

While the app doesn't have any registered events, you can't see any analytics... But it's time to register some!

# AnalyticsPro API

You can use a built-in Swagger UI by clicking on a button in the menu. 

In AnalyticsPro you may either register a single event or a set of multiple events at once. Docs and descriptions are there.

![image](https://user-images.githubusercontent.com/72615475/170873406-629762c4-c691-43d6-8fa5-823efe4c76dd.png)

# Example of sending a query

![image](https://user-images.githubusercontent.com/72615475/170873505-f31a8527-613d-4095-9e46-27a963c70751.png)

![image](https://user-images.githubusercontent.com/72615475/170873520-1c02ad6d-02ab-48f9-8ff7-8f3c2bdcd9d1.png)

# Retvieving analytics data

After uploading the first event the diagram service is now available!

![image](https://user-images.githubusercontent.com/72615475/170873567-b5d2cf14-b5e3-4bf4-bf3e-9b9e9170da9a.png)

If you register the second type of event, you will be able to watch them all and manage queries with tab.

![image](https://user-images.githubusercontent.com/72615475/170873691-4791b404-a643-4989-83c4-c985658f919d.png)

And the third type of event...

![image](https://user-images.githubusercontent.com/72615475/170873735-44c7d775-ad4f-4868-a0c3-703f30b33deb.png)

