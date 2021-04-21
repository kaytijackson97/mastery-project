#Mastery Project

##Plan
Timing will be completed and documented using Trello

* View Reservations for host
    * print header stating view reservation
    * ask for host email
    * print host last name
    * print all host future reservations
* Make a reservation
    * print header stating making reservation
    * ask for guest email
    * ask for host email
    * print host name
    * print host reservations
    * ask user for start and end date
    * check if start and end date are valid (if no, return error message saying its booked)
    * print total
    * offer boolean asking to confirm booking (if no, don't make reservation)
    * make booking
    * display success or not
* Edit a reservation
    * print header stating choose reservation
    * ask for guest email
    * ask for host email
    * print all reservations matching hostId and guestId
    * ask for reservationId
    * confirm reservationId exists
    * ask for startDate (must be blank or date in future)
    * ask for endDate (must be blank or date in future after startDate)
    * check if start and end date are valid (if no, return error message saying its booked)
    * print total
    * offer boolean asking to confirm booking (if no, don't make reservation)
    * edit booking
    * display success or not
* cancel reservation
    * print header stating choose reservation
    * ask for guest email
    * ask for host email
    * print all reservations matching hostId and guestId
    * ask for reservationId
    * confirm reservationId exists
    * remove booking from list of hosts reservations
    * display success or not
    
* Add Host
    * create addHost() in controller to runAppLoop switch statement
    * create addHost in controller as private method
    * add readRequiredEmail to IO
    * add readRequiredPhoneNumber to IO
        * view.addHost() set to new User host
            * create new Host
            * io.readRequiredString for Host Last name set to User.
            * io.readRequiredEmail for email
            * io.readRequiredPhone for phone number
            * io.readRequiredString for address
            * io.readRequiredString for city
            * io.readRequiredString for State
            * io.readRequiredInt for zip code
            * io.readBigDecimal for standard_rate
            * io.readBigDecimal for weekend_rate
        * Result<User> result = hostService.add(host)
            * validate host is not null
            * validate fields are not null or blank
            * validate rates are greater than 0.00
            * validate email address
            * validate phone number
                * validate phone number char at 0 and 3 are parentheses
                * validate phone number is 12 characters long
                * validate phone number only contains numbers and parentheses
            * validate address (getFullAddress)
            * if valid, add user to hostRepository
                * if host is null, return null
                * findAll
                * GUID for host ID
                * add host to all
                * writeAll(all)
                    * printWriter
                    * print HEADER
                    * print all serialized hosts
                        * split full address by delimiter
                        * append ID
                        * append full name
                        * append email
                        * append phone
                        * append address[0] (address)
                        * append address[1] (city)
                        * append address[2] (state)
                        * append address[3] (postal_code)
                        * append getRates[0] (standard_rate)
                        * append getRates[1] (weekend_rate)
                        
* update Host 
    * add update to runAppLoop
    * add update method to controller
        * chooseUser
        * findByEmail
        * add readPhone to IO
        * add readBigDecimal to IO
        * add editHost to view
            * display header
            * set lastName
            * set email
            * set phone
            * set address
            * set city
            * set state
            * set postal code
            * set standard rate
            * set weekend rate
            * return Host
        * add editUser to UserService
        * add editUser to HostService
            * validateUser
            * validateHost
        * add editHost to HostRepository
        * add editHost in HostFileRepository
            * findAll()
            * for loop looping through all hosts
                * if hostId matches, set host in List to parameter
                * writeAll
        * displayStatus
        
* Strech Goal: View reservations by Guest
    * add VIEW_BY_GUEST to mainmenu enum
    * add viewByGuest to runAppLoop
    * 
    
* Stretch Goal: convert to JSON
    * JSON interface with convertToJSON
    * add JSON dependency to pom.xml
        <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>2.5.3</version>
        </dependency>
        
    * create JSON bean in xml dependency-conversion.xml
    * create JSON repository with loose coupling to each repository
    * convertToJSON
        * findAll
            * read csv
            * for each line, deserialize a list of objects from csv
            * return list
        * writeAllToJSON(List<Object> objects)
            * create an ObjectMapper
            * mapper.writeValue(new File(filepath), objects);
        * deserialize
            * take in a string
            * create new object
            * set all fields in object
            * return object
        * RESERVATION ONLY: findAllReservations
            * for each file in 