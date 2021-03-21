package dao.jdbc;

import dao.AccountDao;
import exception.DataAccessException;
import model.Account;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoJdbcImpl implements AccountDao {
    @Override
    public void save(Account account) {
        final String SQL = "insert into ACCOUNT values (DEFAULT,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getName());
            statement.setString(2, account.getAddress());
            statement.setBigDecimal(3, account.getBalance());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getLong(1));
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }

    }

    @Override
    public void update(Account account) {
        final String SQL = "update ACCOUNT set NAME = ?, ADDRESS = ?, BALANCE = ? where id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, account.getName());
            statement.setString(2, account.getAddress());
            statement.setBigDecimal(3, account.getBalance());
            statement.setLong(4, account.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void delete(Account account) {
        final String SQL = "delete FROM ACCOUNT where id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, account.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Optional<Account> findById(Long id) {
        final String SQL = "select * from ACCOUNT where id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Account account = transformCurrentEntry(rs);
                    return Optional.of(account);
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.empty();


    }

    @Override
    public List<Account> findAll() {
        {
            final String SQL = "select * from  ACCOUNT";
            List<Account> result = new ArrayList<>();
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement statement = conn.prepareStatement(SQL);
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Account account = transformCurrentEntry(rs);
                    result.add(account);
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
            return result;
        }
    }

    @Override
    public Optional<Account> findByNameAndAddress(String name, String address) {
        final String SQL = "select * from ACCOUNT where NAME = ? and ADDRESS = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, name);
            statement.setString(2, address);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Account account = transformCurrentEntry(rs);
                    return Optional.of(account);
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.empty();

    }

    private Account transformCurrentEntry(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setName(rs.getString("name"));
        account.setAddress(rs.getString("address"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
