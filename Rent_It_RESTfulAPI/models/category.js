var mongoose = require('mongoose');

var categorySchema = mongoose.Schema({
	name:{
		type:String,
		required:true
	},
	image:{
		type: String
	}
});

var Category = module.exports = mongoose.model('Category',categorySchema);

//Get Category
module.exports.getCategories = function(callback, limit){
	Category.find(callback).limit(limit);
}


//Add Category
/*module.exports.addCategory = function(category, callback){
	Category.create(category, callback);
}*/

//Update Category
/*module.exports.updateCategory = function(id, category, options, callback){
	var query = {_id: id};
	var update = {
		name: category.name
	}
	Category.findOneAndUpdate(query, update, options, callback);
}*/

//Delete category
/*module.exports.removeCategory = function(id, callback){
	var query = {_id:id};
	Category.remove(query, callback);
}*/