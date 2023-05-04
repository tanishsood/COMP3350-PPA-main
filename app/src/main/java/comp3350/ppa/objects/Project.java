package comp3350.ppa.objects;

import android.support.annotation.NonNull;

import java.util.Calendar;

public class Project {
	private int id;
	private String title;
	private String description;
	private Calendar dueDate;
	private String notes;
	private int focusMinutes;

	public Project(int newId) {
		id = newId;
		title = null;
		description = null;
		focusMinutes = 30;
	}

	public Project(int newId, String newProjectTitle, String newProjectDescription, String newNotes, Calendar newDueDate, int newFocusMinutes) {
		if (newProjectTitle.equals(""))
			throw new IllegalArgumentException("Please enter a title");
		if (newProjectTitle == null || newProjectDescription == null || newDueDate == null)
			throw new NullPointerException();
		if (newProjectTitle.length() < 1 || newProjectTitle.length() > 255)
			throw new IllegalArgumentException("Name should be between 1 and 255 characters");
		if (newFocusMinutes < 0)
			throw new IllegalArgumentException("Focus minutes should not be null");
		id = newId;
		title = newProjectTitle;
		description = newProjectDescription;
		notes = newNotes;
		dueDate = newDueDate;
		focusMinutes = newFocusMinutes;
	}
	public int getId() {
		return (id);
	}

	public void setId(int projectId) { this.id = projectId;	}

	public String getTitle() {return (title);}

	public String getDescription() {
		return (description);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		if (notes == null)
			throw new NullPointerException();
		this.notes = notes;
	}

	public Calendar getDueDate() {
		return dueDate;
	}

	public int getFocusMinutes() {
		return focusMinutes;
	}

	public void setFocusMinutes(int focusMinutes) {
		if (focusMinutes < 0)
			throw new IllegalArgumentException("Focus minutes should not be negative");
		this.focusMinutes = focusMinutes;
	}

	@NonNull
	public String toString()
	{
		return "Project: " + id +" " + title + " " + description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Project project = (Project) o;
		return id == project.id && focusMinutes == project.focusMinutes
				&& title.equals(project.title) && description.equals(project.description)
				&& notes.equals(project.notes) && dueDate.equals(project.dueDate);
	}
}