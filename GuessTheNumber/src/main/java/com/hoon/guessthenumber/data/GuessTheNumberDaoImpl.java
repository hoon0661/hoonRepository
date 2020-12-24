/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.data;

import com.hoon.guessthenumber.models.GuessNumber;
import com.hoon.guessthenumber.models.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hoon
 */
@Repository 
@Profile("database")
public class GuessTheNumberDaoImpl implements GuessTheNumberDao{
    private final JdbcTemplate jdbcTemplate;
    
    public GuessTheNumberDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GuessNumber add(GuessNumber number) {
        
        final String sql = "INSERT INTO numbers(number) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, number.getNum());
            return statement;

        }, keyHolder);
        
        number.setId(keyHolder.getKey().intValue());
        
        return number;
    }
    
    @Override
    public Round addRound(Round round) {
        
        final String sql = "INSERT INTO rounds(guessnumber, id, exact, partial, guesseddate) VALUES(?, ?, ?, ?, ?);";

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.SUCCESS_NO_INFO);

            statement.setInt(1, round.getGuessNumber());
            statement.setInt(2, round.getId());
            statement.setInt(3, round.getExactMatches());
            statement.setInt(4, round.getPartialMatches());
            statement.setTimestamp(5, round.getDate());
            
            return statement;

        });
        
        
        return round;
    }

    @Override
    public List<GuessNumber> getAll() {
        final String sql = "SELECT id, number, finished FROM numbers;";
        return jdbcTemplate.query(sql, new NumberMapper());
    }
    
    @Override
    public List<Round> getAllRounds() {
        final String sql = "SELECT guessnumber, id, exact, partial, guesseddate FROM numbers;";
        return jdbcTemplate.query(sql, new RoundMapper());
    }
    
    @Override
    public void update(GuessNumber number) {
        
        final String sql = "Update numbers SET "
                + "number = ?, "
                + "finished = ? "
                + "WHERE id = ?;";
        
        jdbcTemplate.update(sql,
                number.getNum(),
                number.isFinished(),
                number.getId());
             
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM numbers where id = ?;";
        
        return jdbcTemplate.update(sql, id) > 0;
    }
    
    @Override
    public boolean deleteRoundById(int id) {
        final String sql = "DELETE FROM rounds where id = ?;";
        
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public GuessNumber findGameById(int id) {
        final String sql = "SELECT id, number, finished FROM numbers WHERE id = ?;";
        
        return jdbcTemplate.queryForObject(sql, new NumberMapper(), id);
    }
    
    @Override
    public List<Round> findRoundById(int id) {
        final String sql = "SELECT id, guessnumber, exact, partial, guesseddate FROM rounds WHERE id = ? order by guesseddate DESC;";
        
        return jdbcTemplate.query(sql, new RoundMapper(), id);
    }
    
    private static final class NumberMapper implements RowMapper<GuessNumber> {
        
        @Override
        public GuessNumber mapRow(ResultSet rs, int index) throws SQLException {
            GuessNumber num = new GuessNumber();
            num.setId(rs.getInt("id"));
            num.setNum(rs.getInt("number"));
            num.setFinished(rs.getBoolean("finished"));
            
            return num;
        }
    }
    
    private static final class RoundMapper implements RowMapper<Round> {
        
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setGuessNumber(rs.getInt("guessnumber"));
            round.setId(rs.getInt("id"));
            round.setExactMatches(rs.getInt("partial"));
            round.setPartialMatches(rs.getInt("exact"));
            round.setDate(rs.getTimestamp("guesseddate"));
            
            return round;
        }
    }
    
    
}
