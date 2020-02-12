package app;

public class Field {
	private int adjacentBombs;
	private boolean bomb, hidden, flag;
	public Field() {
		adjacentBombs = 0;
		bomb = false;
		hidden = true;
		flag = false;
	}
	public Field(int adjacentBombs, boolean bomb, boolean hidden, boolean flag) {
		this.adjacentBombs = adjacentBombs;
		this.bomb = bomb;
		this.hidden = hidden;
		this.flag = flag;
	}
	public int getAdjacentBombs() {
		return adjacentBombs;
	}
	public void setAdjacentBombs(int adjacentBombs) {
		this.adjacentBombs = adjacentBombs;
	}
	public boolean isBomb() {
		return (bomb);
	}
	public void setBomb() {
		this.bomb = true;
	}
	public void removeBomb() {
		this.bomb = false;
	}
	public boolean isHidden() {
		return (hidden);
	}
	public void reveal() {
		this.hidden = false;
	}
	public boolean flagged() {
		return flag;
	}
	public void setFlag() {
		this.flag = true;
	}
	public void removeFlag() {
		this.flag = false;
	}
}
