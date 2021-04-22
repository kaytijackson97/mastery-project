#Mastery Project
This 3 layer console application was built to act as a fictitious hospitality service for hosts to rent their properties to guests.
Guests and hosts can make profiles which require a name, phone number (validated through regex), email address (validated through
regex), and state. Hosts are required to input property information and rates. All rates/costs are handled with BigDecimal 
variables and all dates through LocalDate.

The User can:
* Exit from the main menu
* View reservations by host email
* Make reservation
* Make guest profile
* Make host profile
* Edit reservations
* Edit guest profile
* Edit host profile
* Permanently delete reservations
* Soft delete guest profile (will delete reservations, but keep profile in a hidden state)
* Soft delete host profile (will delete reservations, but keep profile in a hidden state)

Provided junit testing for domain and repository layer and information is stored in comma delimited CSV files and JSON files.