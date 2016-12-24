package datamodels;

public class User {
	
    private String FirstName;
	private String MiddleName;
	private String LastName;
	private String Contact;
	private String SSN;
	private String Email;
	private String AddressLine1;
	private String AddressLine2;
	private String City;
	private String Username;
	private String Gender;
	private String State;
	private String Password;
	private String dateofbirth;
	
	public String getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getFirstName() {
		return FirstName;
	}
	public String getMiddleName() {
		return MiddleName;
	}
	public String getLastName() {
		return LastName;
	}
	public String getContact() {
		return Contact;
	}
	public String getSSN() {
		return SSN;
	}
	public String getEmail() {
		return Email;
	}
	public String getAddressLine1() {
		return AddressLine1;
	}
	public String getAddressLine2() {
		return AddressLine2;
	}
	public String getCity() {
		return City;
	}
	public String getGender() {
		return Gender;
	}
	public String getState() {
		return State;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	public void setSSN(String sSN) {
		SSN = sSN;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public void setAddressLine1(String addressLine1) {
		AddressLine1 = addressLine1;
	}
	public void setAddressLine2(String addressLine2) {
		AddressLine2 = addressLine2;
	}
	public void setCity(String city) {
		City = city;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public void setState(String state) {
		State = state;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
}
