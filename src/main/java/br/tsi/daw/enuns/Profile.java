package br.tsi.daw.enuns;

public enum Profile {
	
	ADMIN("ADMINISTRADOR"),
	FUNC("FUNCIONARIO"),
	USER("CLIENTE");
	
	private String description;

	private Profile(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
