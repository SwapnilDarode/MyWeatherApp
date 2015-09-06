# Leftshift - Weather Forecast app

My Weather app is a mobile app build as a solution to a problem statement that forms a part of an evaluation process that aims to display current weather for the user location and option to add multiple cities to check the weather details.

  - Displays current weather for user location
  - Provision to add multiple cities based on auto-suggestion list
  - When clicked on info button, displays weather forecast for 14 days for the selected city

The application makes use of openweathermap.org APIs to fetch and display weather forecast:
> Onload, with pre-checks for network and location services, the application fecthes the supported cities list from openweathermap.org.
> Based on the location service status, if enabled, displays current weather for the user location else, prompts user to enable location service.
> Provides option to add cities through a dialog interface that auto-suggest city names as user types in.
> The selected city weather is displayed in a list view that is scrollable to view the weather information for selected cities.
> The application provides an information icon at the bottom right of the daily weather view to fetch 14 day forecast for the selected city.
##### Note:
 - The code does not concentrate on the UI aspects as its not the highlight of the solution, but displays a readable and organized information in the UI.

#### Resources
- The weather information icons from openweathermap.org
- The application makes use of app icons and few other icons from iconfinder.com, with free usage access

#### Tech
 - Android studio
 - picasso image loader lib
 - gson JSON lib

#### Todos
 - Improve UI
 - Code Cleaning
 - Write Tests
 - Add Code Comments
 - Add Night Mode
----

