Problem1

1)
db.createCollection("categories")
db.categories.insert({ _id: "MongoDB", parent: "Databases"})
db.categories.insert({ _id: "dbm", parent: "Databases"})
db.categories.insert({ _id: "Databases", parent: "Programming"})
db.categories.insert({ _id: "Languages", parent: "Programming"})
db.categories.insert({ _id: "Programming", parent: "Books"})
db.categories.insert({ _id: "Books", parent: null})


var ancestors = []
var stack = []
var l = 1
var item = db.categories.findOne({_id: "MongoDB"})
stack.push(item)
while(stack.length>0){
  var current = stack.pop()
  var temp={}
  temp['Name'] = current.parent
  temp['Level'] = l
  ancestors.push(temp)
  l++
  var p =  db.categories.findOne( {_id:current.parent})
  if(p.parent){
    stack.push(p)
  }
}
ancestors

2)
var descendants = [];
var stack = [];
var l=1;
var item = db.categories.findOne({_id: "Books"});
item['level']=1
stack.push(item);
while (stack.length > 0) {
	var current = stack.pop();
    var children =  db.categories.find( {parent: current._id});
	while (children.hasNext() == true) {
		var child = children.next();
		child['level']=current.level+1;
		descendants.push(child);
		stack.push(child);
	}
}
var maxlen=1
while (descendants.length > 0) {
	var son = descendants.pop();
	var len=son.level;
	if(len>maxlen)
		maxlen=len;
}
maxlen

var descendants = [];
var stack = [];
var l=1;
var maxlen=1;
var item = db.categories.findOne({_id: "Books"});
item['level']=1
stack.push(item);
while (stack.length > 0) {
	var current = stack.pop();
    var children =  db.categories.find( {parent: current._id});
	while (children.hasNext() == true) {
		var child = children.next();
		child['level']=current.level+1;
		if(child['level']>maxlen)
			maxlen=child['level'];
		descendants.push(child);
		stack.push(child);
	}
}
maxlen

3)
db.categories.drop()
db.createCollection("categories")
db.categories.insert({ _id: "MongoDB", children: []})
db.categories.insert({ _id: "dbm", children: []})
db.categories.insert({ _id: "Databases", children: [ "MongoDB", "dbm"]})
db.categories.insert({ _id: "Languages", children: []})
db.categories.insert({ _id: "Programming", children: ["Languages", "Databases"]})
db.categories.insert({ _id: "Books", children: ["Programming"]})

db.categories.findOne({children:"dbm"})._id

4)
var descendants = [];
var stack = [];
var item = db.categories.findOne({_id: "Books"});
stack.push(item);
while (stack.length > 0) {
	var current = stack.pop();
    var x = current.children;
	var childrens=db.categories.find({_id: {$in: x}});
	while (childrens.hasNext() == true) {
		var child = childrens.next();
		descendants.push(child._id);
		stack.push(child);
	}
}
descendants

5)
var siblings = db.categories.findOne({children: "Databases"}).children;
siblings.pop("Databases");
siblings;





Problem2

1)
db.createCollection("test")
var mydocuments = [
  {
    "_id" : 1,
    "name" : {
        "first" : "John",
        "last" : "Backus"
    },
    "birth" : ISODate("1924-12-03T05:00:00Z"),
    "death" : ISODate("2007-03-17T04:00:00Z"),
    "contribs" : [
        "Fortran",
        "ALGOL",
        "Backus-Naur Form",
        "FP"
    ],
    "awards" : [
        {
            "award" : "W.W. McDowell Award",
            "year" : 1967,
            "by" : "IEEE Computer Society"
        },
        {
            "award" : "National Medal of Science",
            "year" : 1975,
            "by" : "National Science Foundation"
        },
        {
            "award" : "Turing Award",
            "year" : 1977,
            "by" : "ACM"
        },
        {
            "award" : "Draper Prize",
            "year" : 1993,
            "by" : "National Academy of Engineering"
        }
    ]
},
{
    "_id" : ObjectId("51df07b094c6acd67e492f41"),
    "name" : {
        "first" : "John",
        "last" : "McCarthy"
    },
    "birth" : ISODate("1927-09-04T04:00:00Z"),
    "death" : ISODate("2011-12-24T05:00:00Z"),
    "contribs" : [
        "Lisp",
        "Artificial Intelligence",
        "ALGOL"
    ],
    "awards" : [
        {
            "award" : "Turing Award",
            "year" : 1971,
            "by" : "ACM"
        },
        {
            "award" : "Kyoto Prize",
            "year" : 1988,
            "by" : "Inamori Foundation"
        },
        {
            "award" : "National Medal of Science",
            "year" : 1990,
            "by" : "National Science Foundation"
        }
    ]
},
{
    "_id" : 3,
    "name" : {
        "first" : "Grace",
        "last" : "Hopper"
    },
    "title" : "Rear Admiral",
    "birth" : ISODate("1906-12-09T05:00:00Z"),
    "death" : ISODate("1992-01-01T05:00:00Z"),
    "contribs" : [
        "UNIVAC",
        "compiler",
        "FLOW-MATIC",
        "COBOL"
    ],
    "awards" : [
        {
            "award" : "Computer Sciences Man of the Year",
            "year" : 1969,
            "by" : "Data Processing Management Association"
        },
        {
            "award" : "Distinguished Fellow",
            "year" : 1973,
            "by" : " British Computer Society"
        },
        {
            "award" : "W. W. McDowell Award",
            "year" : 1976,
            "by" : "IEEE Computer Society"
        },
        {
            "award" : "National Medal of Technology",
            "year" : 1991,
            "by" : "United States"
        }
    ]
},
{
    "_id" : 4,
    "name" : {
        "first" : "Kristen",
        "last" : "Nygaard"
    },
    "birth" : ISODate("1926-08-27T04:00:00Z"),
    "death" : ISODate("2002-08-10T04:00:00Z"),
    "contribs" : [
        "OOP",
        "Simula"
    ],
    "awards" : [
        {
            "award" : "Rosing Prize",
            "year" : 1999,
            "by" : "Norwegian Data Association"
        },
        {
            "award" : "Turing Award",
            "year" : 2001,
            "by" : "ACM"
        },
        {
            "award" : "IEEE John von Neumann Medal",
            "year" : 2001,
            "by" : "IEEE"
        }
    ]
},
{
    "_id" : 5,
    "name" : {
        "first" : "Ole-Johan",
        "last" : "Dahl"
    },
    "birth" : ISODate("1931-10-12T04:00:00Z"),
    "death" : ISODate("2002-06-29T04:00:00Z"),
    "contribs" : [
        "OOP",
        "Simula"
    ],
    "awards" : [
        {
            "award" : "Rosing Prize",
            "year" : 1999,
            "by" : "Norwegian Data Association"
        },
        {
            "award" : "Turing Award",
            "year" : 2001,
            "by" : "ACM"
        },
        {
            "award" : "IEEE John von Neumann Medal",
            "year" : 2001,
            "by" : "IEEE"
        }
    ]
},
{
    "_id" : 6,
    "name" : {
        "first" : "Guido",
        "last" : "van Rossum"
    },
    "birth" : ISODate("1956-01-31T05:00:00Z"),
    "contribs" : [
        "Python"
    ],
    "awards" : [
        {
            "award" : "Award for the Advancement of Free Software",
            "year" : 2001,
            "by" : "Free Software Foundation"
        },
        {
            "award" : "NLUUG Award",
            "year" : 2003,
            "by" : "NLUUG"
        }
    ]
},
{
    "_id" : ObjectId("51e062189c6ae665454e301d"),
    "name" : {
        "first" : "Dennis",
        "last" : "Ritchie"
    },
    "birth" : ISODate("1941-09-09T04:00:00Z"),
    "death" : ISODate("2011-10-12T04:00:00Z"),
    "contribs" : [
        "UNIX",
        "C"
    ],
    "awards" : [
        {
            "award" : "Turing Award",
            "year" : 1983,
            "by" : "ACM"
        },
        {
            "award" : "National Medal of Technology",
            "year" : 1998,
            "by" : "United States"
        },
        {
            "award" : "Japan Prize",
            "year" : 2011,
            "by" : "The Japan Prize Foundation"
        }
    ]
},
{
    "_id" : 8,
    "name" : {
        "first" : "Yukihiro",
        "aka" : "Matz",
        "last" : "Matsumoto"
    },
    "birth" : ISODate("1965-04-14T04:00:00Z"),
    "contribs" : [
        "Ruby"
    ],
    "awards" : [
        {
            "award" : "Award for the Advancement of Free Software",
            "year" : "2011",
            "by" : "Free Software Foundation"
        }
    ]
},
{
    "_id" : 9,
    "name" : {
        "first" : "James",
        "last" : "Gosling"
    },
    "birth" : ISODate("1955-05-19T04:00:00Z"),
    "contribs" : [
        "Java"
    ],
    "awards" : [
        {
            "award" : "The Economist Innovation Award",
            "year" : 2002,
            "by" : "The Economist"
        },
        {
            "award" : "Officer of the Order of Canada",
            "year" : 2007,
            "by" : "Canada"
        }
    ]
},
{
    "_id" : 10,
    "name" : {
        "first" : "Martin",
        "last" : "Odersky"
    },
    "contribs" : [
        "Scala"
    ]
}
]

db.test.insert(mydocuments)

var mapFunction1 = function(){
  if(this.awards!=null){
    for(var i = 0; i<this.awards.length; i++){
      emit(this.awards[i].award,1)
    }
  }
}

var reduceFunction1 = function(k,vals){
  return Array.sum(vals)
}

db.test.mapReduce(
mapFunction1,
reduceFunction1,
{out:"problem2_1"}
)
db.problem2_1.find()

2)
db.createCollection("problem2_2")
db.test.find({"birth": {"$exists": true}}).forEach(function(x){db.problem2_2.insert(x)})
db.problem2_2.aggregate([{$group:{_id:{year:{$year:"$birth"}},id:{$push:"$_id"}}}])

3)
var maxid = db.test.findOne({$query:{},$orderby:{_id:-1}})._id
db.test.find({_id:maxid})

var minid = db.test.findOne({$query:{},$orderby:{_id:1}})._id
db.test.find({_id:minid})

4)
db.test.createIndex({ "$**": "text" })
db.test.find({$text: {$search: "\"Turing Award\""}})

5)
db.createCollection("problem2_5")
db.test.createIndex({ "$**": "text" })
db.test.find({$text: {$search: "\"National Medal\""}}).forEach(function(x){db.problem2_5.insert(x)})
db.test.find({$text: {$search: "Turing"}}).forEach(function(x){db.problem2_5.insert(x)})
