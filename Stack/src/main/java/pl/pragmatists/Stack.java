package pl.pragmatists;

public class Stack {

	private int size;
	private int capacity;
	private String[] elements;

	public Stack(int capacity) {
		this.capacity = capacity;
		elements = new String[capacity];
	}

	public int size() {
		return size;
	}

	public void push(String element) {
		if (isFull())
			throw new StackOverflowException();
		this.elements[size++] = element;
	}

	private boolean isFull() {
		return size == capacity;
	}

	public String pop() {
		if (isEmpty())
			throw new StackUnderflowException();
		return elements[--size];
	}

	private boolean isEmpty() {
		return size == 0;
	}

}
