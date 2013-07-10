package pl.pragmatists.refactoring.nullobject;

public class PersonAfter {
	private User user = new NullUser();
	private Session session;

	public void setUser(User user) {
		if (user == null)
			user = new NullUser();
		else
			this.user = user;
	}

	public void addToCart(Item item) {
		Cart cart = user.getCart();
		cart.add(item);
		user.persist();
	}

	public void removeFromCart(Item item) {
		Cart cart = user.getCart();
		cart.remove(item);
		user.persist();
	}

	public void numberOfItems(Item item, int number) {
		Cart cart = user.getCart();
		Item it = cart.retrieve(item);
		it.setQuantity(number);
		user.persist();
	}

	public class Session {

		public Cart getCart() {
			return null;
		}

		public void setUser(User user) {
			// TODO Auto-generated method stub

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

	public class NullUser extends User {

		@Override
		public Cart getCart() {
			return session.getCart();
		}

		@Override
		public void persist() {
			session.setUser(user);
		}
	}
}
