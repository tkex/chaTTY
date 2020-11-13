package client;
import javax.swing.AbstractListModel;

public class UserModel extends AbstractListModel<String> {

  private String[] users = new String[0];
  
  @Override
  public String getElementAt(int index) {
    return users[index];
  }
  
  @Override
  public int getSize() {
    return users.length;
  }

  public void setUsers(String[] users) {
    this.users = users;
    fireContentsChanged(this, 0, getSize());
  }

}
