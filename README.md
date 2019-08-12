# Locus

Locus is an Andorid Application which generalizes Points of Interests (POI) to a person according to the category the place falls in. It uses Google Maps API and Street view within the application. The categories include eateries, grocery stores, fuel stations, restaurants, bars, ATM/ABMs, Hospitals and Hotels etc. It shows description, rating and location of the place with in application navigation through Google Maps. 

It has a unique service named as "LocusRemind" that is defined as a "Location Based Reminder" which is a background service. A person can set up a reminder for a place, along with distance radius, then the application will notify the person with the reminder set if the place is within the radius of distance that person set. For instance, if I want to buy milk, I selected Walmart and a radius of 5 KMs, the application will notify me to get eggs on my phone so I don't forget it. 

This application has "Google Sign In" and it uses SQLite and Firebase Database. Everything is synced through a background service. 
