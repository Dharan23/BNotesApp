# BNotesApp
Simple example using Android Architecture Components, 

What Android Documentation says about these Architecture components is as follows, 

1. DataBinding
The Data Binding Library is a support library that allows you to bind UI components in your layouts to data sources in your app using a   declarative format rather than programmatically.
 
2. ROOM Database
The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the  full power of SQLite.

3. View Model
The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way. The ViewModel class allows data to survive configuration changes such as screen rotations.

4. Live Data
LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.


App Feature's

1. The key feature of app is to take Notes from your android phone and store it locally in your phone.The application uses ROOM database to accomplish that.

2. The application also uses a PIN verfication technique so that you can secure your important notes using your own PIN.

3. The secured notes content is hidden on main notes screen which makes the app more secured to use.

4. The PIN is also stored in to your phone using ROOM Database. So, you dont have to worry about any data leakage as it is safe in your own hand.


