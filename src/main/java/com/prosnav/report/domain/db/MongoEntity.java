package com.prosnav.report.domain.db;

import org.springframework.data.annotation.Id;

public class MongoEntity {
	
	@Id
	protected long _id;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}
}
