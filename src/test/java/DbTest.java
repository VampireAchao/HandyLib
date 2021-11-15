import com.handy.lib.db.Db;

/**
 * @author handy
 * @date 2021/11/15 14:30
 */
public class DbTest {

    public static void main(String[] args) {
        Db<User> userDb = new Db<>(User.class);
        userDb.execution().create();
    }

}
