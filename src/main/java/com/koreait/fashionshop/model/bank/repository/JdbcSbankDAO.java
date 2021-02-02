package com.koreait.fashionshop.model.bank.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.koreait.fashionshop.exception.DepositFailException;
import com.koreait.fashionshop.exception.WithdrawFailException;
import com.koreait.fashionshop.model.common.Bell;

@Repository
public class JdbcSbankDAO implements SbankDAO{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//입금
	public void deposit(int money) throws WithdrawFailException{
		int result = jdbcTemplate.update("insert into sbank(sbank_id, total) values(seq_sbank.nextval,?)", money);
		result=0;
		if(result==0) {
			throw new WithdrawFailException("출금 실패!!");
		}
		}
	}
	

