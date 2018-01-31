package com.capgemini.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialAmountException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;
import com.capgemini.service.AccountService;
import com.capgemini.service.AccountServiceImpl;
public class AccountTest {

	AccountService accountService;
	
	
	@Mock
	AccountRepository accountRepository;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		accountService = new AccountServiceImpl(accountRepository);
	}

	/*
	 * create account
	 * 1.when the amount is less than 500 then system should throw exception
	 * 2.when the valid info is passed account should be created successfully
	 */
	
	@Test(expected=com.capgemini.exceptions.InsufficientInitialAmountException.class)
	public void whenTheAmountIsLessThan500SystemShouldThrowException() throws InsufficientInitialAmountException
	{
		accountService.createAccount(101, 400);
	}
	
	@Test
	public void whenTheValidInfoIsPassedAccountShouldBeCreatedSuccessfully() throws InsufficientInitialAmountException
	{
		Account account =new Account();
		account.setAccountNumber(101);
		account.setAmount(5000);
		when(accountRepository.save(account)).thenReturn(true);
		assertEquals(account, accountService.createAccount(101, 5000));
	}
	
	/*
	 * deposit account
	 * 1.when the account number is invalid should throw exception
	 * 2.when the valid account number is passed deposit should be successfully
	 */
	@Test(expected=com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenInvalidAccountNumberIsPassedShouldThrowInvalidAccountNumberException() throws InvalidAccountNumberException {
		accountService.depositAmount(100, 5000);
	}
	
	@Test
	public void whenValidAccountNumberIsPassedDepositShoudBeSuccessfull() throws InvalidAccountNumberException {
		Account account =new Account();
		account.setAccountNumber(101);
		account.setAmount(5000);
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(10000, accountService.depositAmount(101, 5000));
		
	}
	
	/*
	 * withdrawal account
	 * 1.when the account number is invalid should throw exception
	 * 2.when the amount is invalid should throw exception
	 * 2.when the valid account number is passed withdrawal should be successfully
	 */
	@Test(expected=com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenInvalidAccountNumberIsPassedShouldThrowInvalidAccountNumberExceptionWhileWithdrawal() throws InvalidAccountNumberException, InsufficientBalanceException {		
		accountService.withdrawalAmount(100, 15000);
	}
	
	@Test(expected=com.capgemini.exceptions.InsufficientBalanceException.class)
	public void whenInvalidAmountIsPassedShouldThrowInsufficientBalanceException() throws InvalidAccountNumberException,InsufficientBalanceException, InsufficientInitialAmountException {
		Account account =new Account();
		account.setAmount(5000);
		when(accountRepository.searchAccount(101)).thenReturn(account);
		accountService.withdrawalAmount(101, 15000);
	}
	
	@Test
	public void whenValidAccountNumberIsPassedWithdrawalShoudBeSuccessfull() throws InvalidAccountNumberException, InsufficientBalanceException  {
		Account account =new Account();
		account.setAmount(5000);
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(4000, accountService.withdrawalAmount(101, 1000));
	}

}
