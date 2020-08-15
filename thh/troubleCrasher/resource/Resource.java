package troubleCrasher.resource;

import java.util.LinkedList;

import troubleCrasher.jigsaw.JigsawBoard;

public class Resource {
	LinkedList<JigsawBoard> jigsawBoardList = new LinkedList();
	int hp;
	int stamina;
	int money;
	
	
	public Resource(LinkedList<JigsawBoard> jigsawBoardList, int hp, int stamina, int money) {
		this.jigsawBoardList = jigsawBoardList;
		this.hp = hp;
		this.stamina = stamina;
		this.money = money;
	}


	public LinkedList<JigsawBoard> getJigsawBoardList() {
		return jigsawBoardList;
	}


	public void setJigsawBoardList(LinkedList<JigsawBoard> jigsawBoardList) {
		this.jigsawBoardList = jigsawBoardList;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getStamina() {
		return stamina;
	}


	public void setStamina(int stamina) {
		this.stamina = stamina;
	}


	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}
	

}
