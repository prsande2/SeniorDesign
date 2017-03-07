require('dotenv').config();

// Module dependencies
var express = require('express');
// create instance
var app = express();

var bodyParser = require('body-parser');
var mongoose = require('mongoose');

app.use(bodyParser.json());

Category = require('./models/category');
Item = require('./models/item');

//Connect to Mongoose
var connection_options = {
	user: process.env.MONGO_DB_USER,
	pass: process.env.MONGO_DB_PASS
};
mongoose.connect(process.env.MONGO_DB_URL,
					connection_options);
//mongoose.connect('mongodb://localhost/rent_it');
//mongoose.connect('mongodb://localhost/bookstore');
var db = mongoose.connection;

/*app.get('/', function(req,res){
	res.send('Hello World! Please user /api/books or /api/genres');
});*/

app.get('/api/categories',function(req,res){
	console.log("reached the server");
	Category.getCategories(function(err,categories){
		if(err){
			throw err;
		}
		res.json(categories);
	});
});

/*app.post('/api/categories',function(req,res){
	var genre = req.body;
	Genre.addGenre(genre,function(err,genre){
		if(err){
			throw err;
		}
		res.json(genre);
	});
});

app.put('/api/genres/:_id',function(req,res){
	var id = req.params._id;
	var genre = req.body;
	Genre.updateGenre(id, genre, {}, function(err,genre){
		if(err){
			throw err;
		}
		res.json(genre);
	});
});

app.delete('/api/genres/:_id',function(req,res){
	var id = req.params._id;
	var genre = req.body;
	Genre.removeGenre(id, function(err,genre){
		if(err){
			throw err;
		}
		res.json(genre);
	});
});*/

/*Item.find({}, function(err,items){
	if (err) throw err;

	console.log(items);
});*/
//get all items
app.get('/api/items',function(req,res){
	console.log("reached the server");
	Item.getItems(function(err, items){
		if(err){
			throw err;
		}
		res.json(items);
	});
});
//get item by id
app.get('/api/items/:_id',function(req,res){
	Item.getItemById(req.params._id,function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	})
});
//get my items by uid
app.get('/api/items/user/:uid',function(req,res){
	Item.getItemsByUid(req.params.uid,function(err,items){
		if(err){
			throw err;
		}
		res.json(items);
	})
});
//add new item
app.post('/api/items',function(req,res){
	var item = req.body;
	Item.addItem(item,function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	});
});
/*
app.put('/api/books/:_id',function(req,res){
	var id = req.params._id;
	var book = req.body;
	Book.updateBook(id, book, {}, function(err,book){
		if(err){
			throw err;
		}
		res.json(book);
	});
});
*/
//Delete Item
app.delete('/api/items/:_id',function(req,res){
	var id = req.params._id;
	var item = req.body;
	Item.removeItem(id, function(err,item){
		if(err){
			throw err;
		}
		res.json(item);
	});
});

app.listen(process.env.PORT_NO);
console.log('Running on port 3000...');

// Router
/*var router = require('./routes/router.js')
app.use('/', router);*/

// Setup Server configuration
/*var port = process.env.PORT || 3000;
app.listen(port);
console.log('Node is running on port ' + port);*/

