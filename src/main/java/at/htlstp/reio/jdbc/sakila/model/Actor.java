package at.htlstp.reio.jdbc.sakila.model;

import java.util.Objects;

/**
 * Entity Actor
 * @author reio
 */
public class Actor {
    private Integer id = null;
    private String firstname;
    private String lastname;

    public Actor(String firstname, String lastname) {

        this(null, firstname, lastname);
    }
    
    public Actor(Integer id, String firstname, String lastname) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Integer getId() {
        return id;
    }

    public final void setId(Integer id) {
        if(this.id == null) {
          this.id = id;
        }
        else {
            throw new IllegalStateException("Actor.setId: not null");
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "Actor{" + "id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Actor other = (Actor) obj;
        return Objects.equals(this.id, other.id);
    }
}
