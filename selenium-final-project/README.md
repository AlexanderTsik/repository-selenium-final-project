# selenium-final-project
TBC-SeleniumFinalProject

Features
HolidayPageTests

descendingOrderTest: Validates that sorting by price (most to least expensive) displays the correct order.
ascendingOrderTest: Validates that sorting by price (least to most expensive) displays the correct order.
filterTest: Filters offers for "მთის კურორტები" and validates the results.
priceRangeTest: Filters offers within a price range and validates the results.

LandingPageTests

activeCategoryTest: Verifies category navigation, URL validation, and element color.
logoTest: Ensures the Swoop logo redirects to the homepage.

MoviePageTests

Selects and validates showtimes for movies.
Automates user interactions for seat selection and booking.
Validates registration error messages.

Helper functions:
getAllPricesOnAllPages as the name suggests it goes through all the pages 
on the site and retrieves all the prices and puts them in a list
and then returns the list to us so that we can parse through it 
however we wish 

rgbaToHex simply converts rgba to hex value

P.S
my laptop is basically a toaster and I couldn't run all three browsers at the same 
time hopefully you can