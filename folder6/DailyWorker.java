package com.kartik.worker;

public class DailyWorker extends Worker{
	
	public DailyWorker(String name, int rate) {
		this.name = name;
		this.rate = rate;
	}
	@Override
	public int Pay(int hours) {
		return (hours / 8) * (this.rate);
	}
}
