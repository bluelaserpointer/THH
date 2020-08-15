package troubleCrasher.resource;

import troubleCrasher.engine.TCGame;
import troubleCrasher.jigsaw.Jigsaw;

public class Resource {
	int hp;	// 3HP仅仅是盒子机制
	int stamina;	// 单纯做事情消耗
	int money;	// 买东西
	
	int enemyHp = 2;	// 战斗
	
	int enemyAc;
	int selfAc;
	int selfDc;

	public Resource() {};
	
	public void getAll()
	{
		for(Jigsaw jigsaw:TCGame.jigsawViewer.board().jigsaws())
		{
			System.out.println(((Box)jigsaw).getBoxName());
		}
	}

	
	public int getEnemyHp() {
		return enemyHp;
	}

	public void setEnemyHp(int enemyHp) {
		this.enemyHp = enemyHp;
	}


	public int getEnemyAc() {
		return enemyAc;
	}

	public void setEnemyAc(int enemyAc) {
		this.enemyAc = enemyAc;
	}

	public int getSelfAc() {
		return selfAc;
	}

	public void setSelfAc(int selfAc) {
		this.selfAc = selfAc;
	}

	public int getSelfDc() {
		return selfDc;
	}

	public void setSelfDc(int selfDc) {
		this.selfDc = selfDc;
	}

		
	public Resource(int hp, int stamina, int money) {
		this.hp = hp;
		this.stamina = stamina;
		this.money = money;
	}

	public boolean hasBox(Box box)
	{
		for(Jigsaw jigsaw:TCGame.jigsawViewer.board().jigsaws())
		{
			if(jigsaw.equals(box))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean hasBoxWithName(String name)
	{
		for(Jigsaw jigsaw:TCGame.jigsawViewer.board().jigsaws())
		{
			if(((Box)jigsaw).getBoxName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public void addBoxWithName(String name)
	{
		switch(name)
		{
			case "伤口":
				// TODO: rand
//				1*1
//				1*2
//				1*3 + 1
				// TODO: 
				break;
			case "A":
				break;
			case "B":
				break;
		}
		
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
	
	public boolean delStamina(int quantity)
	{
		int res = this.stamina - quantity;
		if(res <= 0)
		{
			return false;
		}else {
			this.setStamina(res);
			return true;
		}
	}
	
	public boolean delMoney(int quantity)
	{
		int res = this.money - quantity;
		if(res <= 0)
		{
			return false;
		}else {
			this.setMoney(res);
			return true;
		}
	}
	
	public boolean delHp(int quantity)
	{
		int res = this.hp - quantity;
		if(res <= 0)
		{
			this.setHp(0);
			// TODO: 判断玩家是否死亡
			return false;
		}else {
			this.setHp(res);
			return true;
		}
	}
	
	public boolean delEnemeyHp(int quantity)
	{
		int res = this.enemyHp - quantity;
		System.out.println(this.enemyHp + " - " + quantity + " = " + res);
		if(res <= 0)
		{
			this.setEnemyHp(0);
			return false;
		}else {
			this.setEnemyHp(res);
			return true;
		}
	}

}
