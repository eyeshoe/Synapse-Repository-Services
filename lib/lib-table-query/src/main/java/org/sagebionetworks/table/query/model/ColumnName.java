package org.sagebionetworks.table.query.model;

import org.sagebionetworks.table.query.model.visitors.Visitor;

/**
 * This matches &ltcolumn name&gt   in: <a href="http://savage.net.au/SQL/sql-92.bnf">SQL-92</a>
 */
public class ColumnName extends SQLElement {

	Identifier identifier;

	public ColumnName(Identifier identifier) {
		this.identifier = identifier;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void visit(Visitor visitor) {
		visit(identifier, visitor);
	}
	
}
