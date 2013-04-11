package mockito;

public class UserReference {

    private int id;

    public UserReference(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserReference other = (UserReference) obj;
        if (id != other.id)
            return false;
        return true;
    }
    

}
