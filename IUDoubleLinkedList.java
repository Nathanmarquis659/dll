import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * A List Iterator with working listiterator methods is implemented, supports 
 * typical iterator methods as well. 
 * 
 * @author NathanMarquis
 * 
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** 
	 * Creates an empty list with head and tail = null
	 */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	/**
	 * Uses a new DLLIterator with the add method to place the
	 * element at the front of the list.
	 * {@inheritDoc}
	 */
	@Override
	public void addToFront(T element) {
		ListIterator<T> iterator = new DLLIterator();
		iterator.add(element);
	}

	/**
	 * Uses a new DLLIterator starting at end of the list 
	 * with the add method to place the element at the front of the list.
	 * {@inheritDoc}
	 */
	@Override
	public void addToRear(T element) {
		ListIterator<T> iterator = new DLLIterator(size);
		iterator.add(element);
	}

	/**
	 * Uses the addToRear() method, which has the same functionality.
	 * {@inheritDoc}
	 */
	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) { 
		ListIterator<T> iterator = new DLLIterator();
		Boolean isFound = false;
		while (!isFound && iterator.hasNext()) {
			isFound = iterator.next() == target;
		}
		if (!isFound) {
			throw new NoSuchElementException();
		}
		iterator.add(element);
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> iterator = new DLLIterator(index);
		iterator.add(element);
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		ListIterator<T> iterator = new DLLIterator();
		T retVal = iterator.next();
		iterator.remove();
		return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		ListIterator<T> iterator = new DLLIterator(size-1);
		T retVal = iterator.next();
		iterator.remove();
		return retVal;
	}

	@Override
	public T remove(T element) { 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		ListIterator<T> iterator = new DLLIterator();
		Boolean isFound = false;
		T retVal = null;
		while (!isFound && iterator.hasNext()) {
			retVal = iterator.next();
			isFound = retVal == element;
		}
		if (!isFound) {
			throw new NoSuchElementException();
		}
		iterator.remove();
		return retVal;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> iterator = new DLLIterator(index);
		T retVal = iterator.next();
		iterator.remove();
		return retVal;
	}

	@Override
	public void set(int index, T element) { 
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> iterator = new DLLIterator(index);
		iterator.next();
		iterator.set(element);
	}

	@Override
	public T get(int index) { //Update
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> iterator = new DLLIterator(index);
		T retVal = iterator.next();
		return retVal;
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
            this(0);
        }   

        /**
         * Initialize iterator before starting index
         * @param startingIndex the index to start in front of
         */
        public DLLIterator(int startingIndex) {
			if (startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}

            nextNode = head;

			//Only efficient in the first half, can be written to back up from tail in the back half with
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
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
            return (nextNode != head);
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
			T retVal;
			if (!hasNext()) {
				nextNode = lastReturnedNode = tail;
				retVal = tail.getElement();
			} else {
				lastReturnedNode = nextNode = nextNode.getPrevious();
				retVal = lastReturnedNode.getElement();
			}
            nextIndex--;
            return retVal;
        }

        @Override
        public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
            return nextIndex;
        }

        @Override
        public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
            return nextIndex - 1;
        }

        @Override
        public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}

			if (size == 1) {
				head = tail = nextNode = null;
			} else if (lastReturnedNode != nextNode) { //last move was next
				if (lastReturnedNode == head) {
					head = nextNode;
					head.setPrevious(null);
				} else if (lastReturnedNode == tail) {
					tail = tail.getPrevious();
					tail.setNext(null);
				} else {
					lastReturnedNode.getPrevious().setNext(nextNode);
					nextNode.setPrevious(lastReturnedNode.getPrevious());
				}

				nextIndex--;
			} else if (lastReturnedNode == nextNode) { //last move was previous
				if (lastReturnedNode == head) {
					head = head.getNext();
					head.setPrevious(null);
				} else if (lastReturnedNode == tail) {
					tail = tail.getPrevious();
					tail.setNext(null);
				} else {
					nextNode.getPrevious().setNext(nextNode.getNext());
					nextNode.getNext().setPrevious(nextNode.getPrevious());
				}
			}
			lastReturnedNode = null;
			size--;
			iterModCount++;
			modCount++;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}

			lastReturnedNode.setElement(e);
			iterModCount++;
			modCount++;
        }

        @Override
        public void add(T e) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			Node<T> newNode = new Node<T>(e);
            if (size == 0) {
				head = tail = newNode;
				newNode.setNext(nextNode);
			} else if (size > 0) {
				if (nextNode == null) { //tail
					tail.setNext(newNode);
					newNode.setPrevious(tail);
					tail = newNode;
					tail.setNext(nextNode);
				} else if (nextNode == head){ //head
					head.setPrevious(newNode);
					newNode.setNext(head);
					head = newNode;
				} else {
					newNode.setNext(nextNode);
					newNode.setPrevious(nextNode.getPrevious());
					nextNode.getPrevious().setNext(newNode);
					nextNode.setPrevious(newNode);
				}
			}
			lastReturnedNode = null;
			nextIndex++;
			size++;
			iterModCount++;
			modCount++;
        }
    }
}