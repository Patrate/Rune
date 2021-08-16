package fr.emmuliette.rune.mod.spells.properties;

public class EnumElement{
	private String value;
	private Grade grade;
	public EnumElement(String value, Grade grade) {
		this.setValue(value);
		this.setGrade(grade);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
}