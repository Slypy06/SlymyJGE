package fr.slypy.slymyjge.utils;

public class SimpleEntry<K, V>{

	K key;
	V value;
	
	public SimpleEntry(K key, V value) {
	
		this.key = key;
		this.value = value;
		
	}
	
	public K getKey() {

		return key;
		
	}

	public V getValue() {

		return value;
		
	}

	public void setValue(V value) {

		this.value = value;
		
	}

}
