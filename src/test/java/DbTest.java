import com.handy.lib.db.Db;

/**
 * @author handy
 */
public class DbTest {

    public static void main(String[] args) {
        Db<User> userDb = new Db<>(User.class);
        userDb.execution().create();
    }

}
