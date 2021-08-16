import com.handy.lib.db.Db;
import com.handy.lib.db.User;

/**
 * @author handy
 */
public class DbTest {

    public static void main(String[] args) {
        dbTest();
    }

    /**
     * 测试db
     */
    public static void dbTest() {
        Db<User> db = new Db<>(User.class);
        db.builder().eq(true, User::getNickName, "1")
                .eq(false, User::getLoginName, 1);
        User user = db.selectOne();
    }

}