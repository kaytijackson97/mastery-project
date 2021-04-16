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

##File Breakdown
###data directory - sibling of src
* reservations directory
* guests.csv
* hosts.csv
###repository
* DataAccessException extends Exception
    * constructor: super(message)
    
* ReservationFileRepository implementing reservationRepository
    * Fields: String repository, String HEADER, String delimiter, string delimiterReplacement
    * Constructor: repository
    * @Override: findById()
        * create arrayList reservations
        * try buffer reader new file reader getFilePath with host-id
            * read header
            * for loop to read lines until no more lines
                * split line into String[] using comma as delimiter
                * if correct number of expected fields add deserialized string to reservation arrayList
            * catch FileNotFoundException don't do anything
            * catch IOException throw new DataAccessException
        * return list of reservations
    * @Override: add(Reservation reservation)
        * if reservation == null, return null
        * find all reservations from host
        * set reservation id to id of last reservation on list plus 1
        * add reservation to all
        * write all reservations to file
        * return reservation
    * @Override: update()
    * @Override: delete()
    * writeAll
        * try printWriter getFilePath hostId
        * write HEADER
        * enhanced for loop - for each of the reservations, serialize them and write to new line
        * catch FileNotFound exception throw new DataAccessException
    * serialize
        * String builder
        * append reservationId and delimiter
        * append startDate and delimiter
        * append endDate and delimiter
        * append hostId and delimiter
        * append guestId
        * return builder.toString
    * deserialize(String[] fields, String hostId)
        * create reservation
        * reservation.setId(fields[0])
        * reservation.setStartDate(fields[1])
        * reservation.setEndDate(fields[2])
        * create new Host host
        * host.setId(fields[3])
        * reservation.setHost(host)
        * create new Guest guest
        * guest.setId(fields[4])
        * reservation.setGuest(guest)
        * return reservation
    
* ReservationRepository interface
    * findById()
    * add()
    * update()
    * delete()
    
* GuestFileRepository implements UserRepository
    * @Override: findByEmail()
        * create arrayList guests
            * try buffer reader new file reader getFilePath with guest.csv
                * read header
                * for loop to read lines until no more lines
                    * split line into String[] using comma as delimiter
                    * if correct number of expected fields add deserialized string to guests arrayList
                * catch FileNotFoundException don't do anything
                * catch IOException throw new DataAccessException
        * stream guests findFirst and return that guest
        
* HostFileRepository implements UserRepository
    * @Override: findByEmail()
        * create arrayList hosts
            * try buffer reader new file reader getFilePath with hosts.csv
                * read header
                * for loop to read lines until no more lines
                    * split line into String[] using comma as delimiter
                    * if correct number of expected fields add deserialized string to hosts arrayList
                * catch FileNotFoundException don't do anything
                * catch IOException throw new DataAccessException
        * stream hosts findFirst and return that host
        
* UserRepository interface
    * findByEmail()
    
###domain
* Response
    * Fields: ArrayList<String> messages
        * getErrorMessages()
        * addErrorMessage()
        * isSuccess()
        
* Result implements Response
    * Field: payload
    * getter and setter for payload
        
* ReservationService
    * Fields: Result result and ReservationRepository repository
    * findById()
        * create result
        * validateHost()
        * repository.findById()
        * validateList()
        * return result
    * isReservationAvailable(reservation)
        * create result
        * validateDates()
        * validateIsNotDoubleBooked()
        * getPrice(reservation)
        * set result payload to reservation
        * return result
    * getPrice()
        * Host host = reservation.getHost
        * LocalDate startDate = reservation.getStartDate
        * LocalDate endDate = reservation.getStartDate
        * BigDecimal standardRate = host.getStandardRate
        * BigDecimal weekendRate = host.getWeekendRate
        * BigDecimal total = new BigDecimal
        * Period stay = startDate.until(endDate)
        * do/while loop while endDate is after startDate
            * check the day of week, if weekend, add weekendRate to total, else add standardRate to total
    * addReservation()
        * create result
        * validateFields()
        * validateIsNotDoubleBooked()
        * repository.add()
        * validateReservation()
        * set result payload to reservation
        * return result
    * validateHost()
        * validate Host is not null or blank, otherwise add error
    * validateFields()
        * validateHost()
        * validate Guest is not null or blank, otherwise add error
        * validateDates()
    * validateDates()
        * validate StartDate is not null and not in past, otherwise add error
        * validate endDate is not null, not in past, and after startDate.plusDays(1), otherwise add error
    * validateList()
        * validate list is not null or 0, otherwise add error
    * validateIsNotDoubleBooked()
        * repository.findAll(hostId)
        * validate that no dates in period for attempted booking existing in the period of current reservations, 
        otherwise add error - need to flesh out a bit more
    * validateReservation()
        * validate reservation is not null, otherwise add error

* GuestService implements UserService
    * @Override: findByEmail()
        * create result
        * validateEmail()
        * repository.findByEmail()
    * validateEmail()
        * validates email is not null, contains '@' character, and contains '.' after '@', otherwise add error

* HostService() implements UserService
    * @Override: findByEmail()
        * creates result
        * validateEmail()
        
    * validateEmail()
        validates email is not null, contains '@' character, and contains '.' after '@', otherwise add error
    
* UserService()
    * findByEmail()
    
###ui
* Controller
    * Fields: ReservationService reservationService
    * run()
        * displayHeader with Welcome
        * try runAppLoop catch errors and display errors
        * displayHeader with Goodbye
    * runAppLoop()
        * do/ while loop while exit is not chose
            * switchStatement on MainMenu enum
                * viewReservations()
                * makeReservation()
                * editReservation()
                * deleteReservation()
    * private viewReservations()
        * displayHeader()
        * String email = view.chooseHost()
        * Host host = hostService.findByEmail(email)
        * List<Reservation> reservations = reservation.findById(host.getId)
    * private makeReservation()
        * displayHeader()
        * String guestEmail = chooseGuest()
        * Guest guest = guest.findByEmail(guestEmail)
        * String hostEmail = chooseHost()
        * Host host = host.findByEmail(hostEmail)
        * List<Reservation> reservations = reservationService.findById(host.getId)
        * LocalDate startDate = choseStartDate()
        * LocalDate endDate = choseEndDate(startDate)
        * create new Reservation reservation 
        * boolean isAvailable = reservationService.isReservationAvailable(startDate, endDate, hostId)
        * boolean isGoingToBook = view.displayIsAvailable(isAvailable);
        * Reservation reservation = reservationService.addReservation(reservation)
        * if result is success - view.displayStatus(true, successMessage)
        * else - view.displayStatus(false, getErrorMessages)
        
* View
    * chooseHost()
        * io.readRequiredString()
        * return string
    * chooseGuest()
        * io.readRequiredString()
        * return string
    * chooseStartDate()
        * do/while true
            * LocalDate date = io.readDate
            * if date is in future, return date
    * chooseEndDate()
        * do/while true
                * LocalDate date = io.readDate
                * if date is in future AND is after start date.plusDays(1), return date
    * displayHeader()
            * io.readString()
    * displayStatus(boolean status, String message)
        * displayStatus(status, List.of(message))
    * displayStatus(boolean status, List<messages>)
        * displayStatus(status, List<messages>)
        * for each message, print it out
        
* consoleIO
    * Field: DateTimeFormatter formatter = DateTimeFormatter.patternOf("MM/DD/YYYY")
    * readString()
    * readRequiredString()
    * readDate()
        * do/while true
            * readRequiredString()
            * try to parse string as date with format. return date
            * catch by printing invalid date error
    
###Model
* Reservation
    * Fields
        * String reservationId
        * LocalDate startDate
        * LocalDate endDate
        * Host host
        * Guest guest
        * BigDecimal price
    * getters and setters for all fields
    
* Host
    * Fields
        * String id
        * String last_name
        * String email
        * String phone
        * String address
        * String city
        * String state
        * int postal_code
        * BigDecimal standard_rate
        * BigDecimal weekend_rate
        
* Guest
    * Fields
        * String guest_id
        * String first_name
        * String last_name
        * String email
        * String phone
        * String state
        
* MainMenu enum
    * EXIT
    * VIEW_RESERVATIONS
    * MAKE_RESERVATION
    * EDIT_RESERVATION
    * DELETE_RESERVATION
###App
* main() which runs whole program