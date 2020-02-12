package sts.test.validator;

public class Implementor implements Validator{
	public Implementor(){
		
	}
	private byte stx= 0x02;
	private byte etx= 0x03;
	private byte dle= 0x10;
	private byte lrc= 0;
	@Override
	public boolean isValid(byte[] message) {
		//checks to see if first byte is an stx byte, if not message error and returns false
		if(message[0]!=stx) {
			System.out.println("no stx byte");
			return false;
		}
		//checks to see if second last byte is an etx byte, if not prints message and returns false
		if(message[message.length-2]!=etx) {
			System.out.println("no ext byte");
			return false;
		}
		for(int i=1; i<message.length-2; i++) {
			//checks to see if the next byte is an stx or etx byte
			if(message[i]==stx||message[i]==etx) {
				System.out.println("no escape character used");
				return false;
			}
			//checks to see if an escape byte is properly used, if so the escaped byte is added
			//to the checksum and i is incremented
			if(message[i]==dle) {
				if(message[i+1]==stx||message[i+1]==etx||message[i+1]==dle) {
					lrc ^= message[i+1];
					i++;
				}
				//if an escape byte is used for a non-escapable byte, 
				//method prints a message and returns false
				else {
					System.out.println("escape used for non escapeable character");
					return false;
				}
			}
			//if byte is normal it is added to the checksum
			else {
				lrc ^= message[i];
			}
		}
		//etx byte is added to checksum and checksum is checked to see if it matches calculated checksum,
		//if it does not much a message is printed and method returns false
		lrc ^= etx;
		if(lrc!=message[message.length-1]) {
			System.out.println("bad checksum");
			return false;
		}
		return true;
	}
	
}
