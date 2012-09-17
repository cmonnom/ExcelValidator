package net.morphbank.validation;



public class CreateValidation {

	Validation validationType; //Row or Column validation
	Integer testId; //id of method to be run
	Object[] args; //arguments for the method being run
	
	public CreateValidation(Validation validationType, Integer testId,
			Object[] args) {
		super();
		this.validationType = validationType;
		this.testId = testId;
		this.args = args;
	}

	public boolean run() throws Exception {
		return validationType.chooseMethod(testId, args);
	}
	
	
	
	
	public Validation getValidationType() {
		return validationType;
	}

	public void setValidationType(Validation validationType) {
		this.validationType = validationType;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	
	
	


	

	
	
	
	
	
}
