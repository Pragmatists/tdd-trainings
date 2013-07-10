package pl.pragmatists.refactoring.nullobject;

public class PersonBefore {
	private User user;
	private Session session;

	public void addToCart(Item item) {
		if (user == null) {
			session.getCart().add(item);
		} else {
			Cart cart = user.getCart();
			cart.add(item);
			user.persist();
		}
	}

	public void removeFromCart(Item item) {
		if (user == null) {
			session.getCart().remove(item);
		} else {
			Cart cart = user.getCart();
			cart.remove(item);
			user.persist();
		}
	}

	public void numberOfItems(Item item, int number) {
		if (user == null) {
			Item it = session.getCart().retrieve(item);
			it.setQuantity(number);
		} else {
			Cart cart = user.getCart();
			Item it = cart.retrieve(item);
			it.setQuantity(number);
			user.persist();
		}
	}

	public class CartRepository {

		public Cart getCartFor(User user) {
			// TODO Auto-generated method stub
			return null;
		}

		public void persist(Cart cart) {
			// TODO Auto-generated method stub

		}

	}

	public class Session {

		public Cart getCart() {
			return null;
		}

	}

	public class Cart {

		public void add(Item item) {
			// TODO Auto-generated method stub

		}

		public void remove(Item item) {
			// TODO Auto-generated method stub

		}

		public Item retrieve(Item item) {
			return item;
			// TODO Auto-generated method stub

		}

	}

	public class Item {

		public void setQuantity(int number) {
			// TODO Auto-generated method stub

		}

	}

	public class User {

		public Cart getCart() {
			// TODO Auto-generated method stub
			return null;
		}

		public void persist() {
			// TODO Auto-generated method stub

		}
	}
}
