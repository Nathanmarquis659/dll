import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is supported.
 * 
 * @author NathanMarquis
 * 
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newHead = new Node<T>(element);
		newHead.setNext(head);
		if (head != null) {
			head.setPrevious(newHead);
		}
		head = newHead;
		if (tail == null) {
			tail = head;
		}
		size ++;
		modCount++;
	}

	@Override
	public void addToRear(T element) { //Update
		Node<T> newTail = new Node<T>(element);
		if (!isEmpty()) {
			tail.setNext(newTail);
		} else {
			head = newTail;
		}
		tail = newTail;
		size ++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) { //Update
		Node<T> newNode = new Node<T>(element);
		Node<T> currentNode = head;
		boolean isFound = false;
		while (currentNode != null && !isFound) {
			if (currentNode.getElement().equals(target)) {
				isFound = true;
				if (currentNode == tail) {
					tail = newNode;
				} else {
					newNode.setNext(currentNode.getNext());
				}
				currentNode.setNext(newNode);
			} else {
				currentNode = currentNode.getNext();
			}
		}
		
		if (!isFound) {
			throw new NoSuchElementException();
		}
		size ++;
		modCount++;
	}

	@Override
	public void add(int index, T element) { //Update
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addToFront(element);
		} else if (index == size) {
			addToRear(element);
		} else {
			Node<T> currentNode = head;
			Node<T> addNode = new Node<>(element);
			for (int i=0; i < index-1; i++) {
				currentNode = currentNode.getNext();
			}
			addNode.setNext(currentNode.getNext());
			currentNode.setNext(addNode);
			size ++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		head = head.getNext();
		if (head == null) {
			tail = null;
		} else {
			head.setPrevious(null);
		}

		size --;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		tail = tail.getPrevious();
		if (tail == null) {
			head = null;
		} else {
			tail.setNext(null);
		}

		size --;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) { //Update
		Node<T> currentNode = head;
		Node<T> previousNode = null;
		boolean isFound = false;
		T retVal = null;

		while (currentNode != null && !isFound) {
			if (currentNode.getElement().equals(element)) {
				isFound = true;
			} else {
				previousNode = currentNode;
				currentNode = currentNode.getNext();
			}
		}
		if (!isFound) {
			throw new NoSuchElementException();
		}

		retVal = currentNode.getElement();
		if (currentNode == head) {
			head = head.getNext();
		} else if (currentNode == tail) {
			previousNode.setNext(null);
			tail = previousNode;
		} else {
			previousNode.setNext(currentNode.getNext());
		}

		size--;
		modCount++;
		return retVal;


	}

	@Override
	public T remove(int index) { //Update
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currentNode = head;
		Node<T> previousNode = null;
		T retVal = null;

		for (int i = 0; i < index; i++) {
			previousNode = currentNode;
			currentNode = currentNode.getNext();
		}
		retVal = currentNode.getElement();
		if (currentNode == head) {
			head = head.getNext();
		} else if (currentNode == tail) {
			previousNode.setNext(null);
			tail = previousNode;
		} else {
			previousNode.setNext(currentNode.getNext());
		}

		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) { //Update
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currentNode = head;

		for (int i = 0; i < index; i++) {
			currentNode = currentNode.getNext();
		}
		
		currentNode.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) { //Update
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currentNode = head;

		for (int i = 0; i < index; i++) {
			currentNode = currentNode.getNext();
		}
		
		return currentNode.getElement();
	}

	@Override
	public int indexOf(T element) { //Update
		Node<T> currentNode = head;
        int currentIndex = 0;
        while (currentNode != null && !currentNode.getElement().equals(element)) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        if (currentNode == null) {
            currentIndex = -1;
        }
		return currentIndex;
	}

	@Override
	public T first() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

    @Override
    public String toString() {
        if (isEmpty()) return "[]";

		Node<T> currentNode = head;
        StringBuilder b = new StringBuilder();
        b.append('[');

        while (currentNode != null) {
            b.append(String.valueOf(currentNode.getElement()));
            b.append(", ");
			currentNode = currentNode.getNext();
        }
		b.delete(b.length()-2, b.length());
		b.append(']');
        return b.toString();
    }

	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

    /** List iterator for double linked lists */
    private class DLLIterator implements ListIterator<T> {

        private Node<T> nextNode;
        private Node<T> lastReturnedNode; // Dual use when trying to remove or check if able to remove
        private int nextIndex;
        private int iterModCount;

        /** Initialize before first element */
        public DLLIterator() {
            nextNode = head;
            nextIndex = 0;
            iterModCount = modCount;
            lastReturnedNode = null;
        }   

        /**
         * Initialize iterator before starting index
         * @param startingIndex the index to start in front of
         */
        public DLLIterator(int startingIndex) {
            nextNode = head;
            for (int i = 0; i < startingIndex; i++) {
                nextNode = nextNode.getNext();
            }
            nextIndex = startingIndex;
            iterModCount = modCount;
            lastReturnedNode = null;
        }        

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T retVal = nextNode.getElement();
			lastReturnedNode = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            return nextNode != head;
        }

        @Override
        public T previous() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'previous'");
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'remove'");
        }

        @Override
        public void set(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }

        @Override
        public void add(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
        }
        
    }
}