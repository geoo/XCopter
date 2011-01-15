
public class Data {
	double oben;
	double unten;
	double front;
	double frontUnten;
	double frontOben;
	double auftrieb;
	
	/**
	 * @return the oben
	 */
	public synchronized double getOben() {
		return oben;
	}

	/**
	 * @param oben
	 *            the oben to set
	 */
	public synchronized void setOben(double oben) {
		this.oben = oben;
	}

	/**
	 * @return the unten
	 */
	public synchronized double getUnten() {
		return unten;
	}

	/**
	 * @param unten the unten to set
	 */
	public synchronized void setUnten(double unten) {
		this.unten = unten;
	}

	/**
	 * @return the front
	 */
	public synchronized double getFront() {
		return front;
	}

	/**
	 * @param front the front to set
	 */
	public synchronized void setFront(double front) {
		this.front = front;
	}

	/**
	 * @return the frontUnten
	 */
	public synchronized double getFrontUnten() {
		return frontUnten;
	}

	/**
	 * @param frontUnten the frontUnten to set
	 */
	public synchronized void setFrontUnten(double frontUnten) {
		this.frontUnten = frontUnten;
	}

	/**
	 * @return the frontOben
	 */
	public synchronized double getFrontOben() {
		return frontOben;
	}

	/**
	 * @param frontOben the frontOben to set
	 */
	public synchronized void setFrontOben(double frontOben) {
		this.frontOben = frontOben;
	}

	/**
	 * @return the auftrieb
	 */
	public synchronized double getAuftrieb() {
		return auftrieb;
	}

	/**
	 * @param auftrieb the auftrieb to set
	 */
	public synchronized void setAuftrieb(double auftrieb) {
		this.auftrieb = auftrieb;
	}
}
