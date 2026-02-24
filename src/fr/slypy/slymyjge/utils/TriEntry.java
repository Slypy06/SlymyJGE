package fr.slypy.slymyjge.utils;

public class TriEntry<S, T, U>{

	S first;
	T second;
	U third;
	
	public TriEntry(S first, T second, U third) {
	
		this.first = first;
		this.second = second;
		this.third = third;
		
	}

	public S getFirst() {
		return first;
	}

	public void setFirst(S first) {
		this.first = first;
	}

	public T getSecond() {
		return second;
	}

	public void setSecond(T second) {
		this.second = second;
	}

	public U getThird() {
		return third;
	}

	public void setThird(U third) {
		this.third = third;
	}

}
