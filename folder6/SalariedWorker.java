package com.kartik.worker;

public class SalariedWorker extends Worker{
	public SalariedWorker(String name, int rate) {
		this.name = name;
		this.rate = rate;
	}
	@Override
	public int Pay(int hours) {
		return this.rate * (hours / 7);
	}
}
