package xyz.scropy.seconomy.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import xyz.scropy.seconomy.database.DatabaseObject;

import java.util.UUID;

@DatabaseTable(tableName = "users")
public class User extends DatabaseObject {

    @DatabaseField(id = true)
    private UUID uniqueId;

    @DatabaseField
    private double money;

    public User() {
    }

    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
