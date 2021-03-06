package ba.bitcamp.homework17.part02.task04;

import java.util.Arrays;

import ba.bitcamp.homework17.part01.task01.Computer;
import ba.bitcamp.homework17.part01.task02.Network;
import ba.bitcamp.homework17.part01.task03.Server;
import ba.bitcamp.homework17.part01.task04.Client;
import ba.bitcamp.homework17.part02.task01.Functionable;
import ba.bitcamp.homework17.part02.task02.ArrayManipulation;

/**
 * StarNetwork extends Network class, and implements Functionable interface
 * which has one method that checks if our network is in function
 * 
 * @author Mladen13
 *
 */
public class StarNetwork extends Network implements Functionable {

	private int counter = 0;
	private Server s;

	/**
	 * <h1>Creating constructor that initializes server</h1>
	 * <p>
	 * 
	 * @param networkName
	 *            - represents name of the network
	 * @param s
	 *            - represents server
	 */
	public StarNetwork(String networkName, Server s) {
		super(networkName);
		this.s = s;
	}

	/**
	 * This method checks if our network is in function. Network is in function
	 * when number of computer on network is lower than server capacity
	 */
	@Override
	public boolean isFunctioning() throws IllegalArgumentException {
		int counter = 0;
		System.out.println(Arrays.toString(getArrayComputer()));
		for (int i = 0; i < getArrayComputer().length; i++) {
			if (getArrayComputer()[i] instanceof Client) {
				Client c = (Client) getArrayComputer()[i];
				if (c.getConnectedNetwork() != null)
					counter++;
			}
		}
		if (counter > s.getMaxNumOfComputers())
			return false;
		else
			return counter <= s.getMaxNumOfComputers();
	}

	/**
	 * This method will connect one computer to server
	 */
	@Override
	public void addComputer(Computer c) {

		if (c instanceof Server) {
			throw new IllegalArgumentException("You can't add server.");
		} else {
			// Extending our array by one
			Computer[] cmp = ArrayManipulation.extendArray(getArrayComputer());
			cmp[cmp.length - 1] = c;
			setArrayComputer(cmp);
			if (c instanceof Client) {
				Client cl = (Client) c;
				// Connecting computer to server
				cl.connect(s);
			}
		}
	}

	/**
	 * This method will remove one computer from network
	 */
	@Override
	public void removeComputer(Computer c) {
		if (c instanceof Client) {
			Client cl = (Client) c;
			for (int i = 0; i < getArrayComputer().length; i++) {
				// Checking if there is wanted computer in our computer array
				if (c.equals(getArrayComputer()[i])) {
					ArrayManipulation.shrinkArray(getArrayComputer(), i);
					setArrayComputer(ArrayManipulation.shrinkArray(
							getArrayComputer(), i));
					if(c instanceof Client){
						cl.disconnect();	
					} else 
						throw new IllegalArgumentException();
					return;
					
				}
			}
		} else {
			throw new IllegalArgumentException("You can't remove server.");
		}
		throw new IllegalArgumentException("Computer not found!");

	}

	/**
	 * Method that prints number of computers, and checks if network is
	 * functioning
	 */
	public String toString() {

		return "Number of computers on server: " + getArrayComputer().length
				+ "\nNetwork in function: " + isFunctioning();
	}

}
