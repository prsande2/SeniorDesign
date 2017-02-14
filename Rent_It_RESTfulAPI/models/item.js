var mongoose = require('mongoose');

var itemSchema = mongoose.Schema({
	uid:{
		type:String,
		required:true
	},
	title:{
		type:String,
		required:true
	},
	description:{
		type:String
	},
	condition:{
		type:String
	},
	location:{
		type:Array
	},
	city:{
		type:String,
		required:true
	},
	zipcode:{
		type:Number,
		required:true
	},
	tags:{
		type:Array
	},
	value:{
		type:Number
	},
	rate:{
		type:Number,
		required:true
	},
	image:{
		type:String
	},
	visible:{
		type:Boolean,
		default: true
	},
	created_date:{
		type: Date,
		default: Date.now
	}
});

var Item =  module.exports = mongoose.model('Item',itemSchema);


//Get All Item
module.exports.getItems = function(callback, limit){
	Item.find(callback).limit(limit);
}

//Get Items by Id
module.exports.getItemById = function(id, callback){
	Item.findById(id, callback);
}

//Get My Item with uid
module.exports.getItemsByUid = function(uid, callback){
	Item.findOne({'uid': uid}, callback);
	//Item.where('uid', uid).findOne(callback);
}

//Add Item
module.exports.addItem = function(item, callback){
	Item.create(item, callback);
}
/*
//Update Book
module.exports.updateBook = function(id, book, options, callback){
	var query = {_id: id};
	var update = {
		title: book.title,
		genre: book.genre,
		description: book.description,
		author: book.author,
		publisher: book.publisher,
		image_url: book.image_url,
		buy_url:book.buy_url
	}
	Book.findOneAndUpdate(query, update, options, callback);
}
*/
//Delete Item
module.exports.removeItem = function(id, callback){
	//var query = {_id:id};
	Item.remove({'_id':id}, callback);
}




