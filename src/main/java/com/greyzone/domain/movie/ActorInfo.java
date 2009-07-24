/*
 * This file is part of JMoviedb.
 * 
 * Copyright (C) Tor Arne Lye torarnelye@gmail.com
 * 
 * JMoviedb is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMoviedb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.greyzone.domain.movie;

public class ActorInfo implements Comparable<ActorInfo> {
	private Person actor;
	private int id;
	private String character;
	
	public ActorInfo(int id, Person actor, String character) {
		this.setId(id);
		this.setPerson(actor);
		this.setCharacter(character);
	}
	
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String as) {
		this.character = as;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Person getPerson() {
		return actor;
	}
	public void setPerson(Person actor) {
		this.actor = actor;
	}
	
	public String toString() {
		return actor.getName() + " as " + character;
	}

	public int compareTo(ActorInfo o) {
		return this.id - o.id;
	}
	
}
