var express = require('express');
var router = express.Router();

// GET Request
router.get('/', function(req, res) {
	res.json({
		message: 'You have reached Mizumi Malhan'
	});
});


// Register as module
module.exports = router;