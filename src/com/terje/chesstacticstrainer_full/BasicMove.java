package com.terje.chesstacticstrainer_full;

import com.terje.chesstacticstrainer_full.Types.Cord;



public class BasicMove {

		protected Cord from,to;
		
		public BasicMove() {
			
		}
		
		public BasicMove(Cord from, Cord to) {
			this.from=from;
			this.to=to;
		}
		
		
		public int getFromX() {
			return from.column;
		}
		
		public int getFromY() {
			return from.row;
		}
		
		public int getToX() {
			return to.column;
		}
		
		public int getToY() {
			return to.row;
		}
		
		
		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
		    if (other == this) return true;
		    if (!(other instanceof BasicMove))return false;
		    Move mother = (Move)other;
			return (this.from.equals(mother.from)&&
					this.to.equals(mother.to));
		}
}

