var pXa = 0;
var pYa = 0;

var mapInfo = {
    titleName: "M棟3樓組/測",
    x: 680,
    y: 0
};

var titleGroup = [
    //assy
    {lineName: "L1-1", x: 305, y: 430},
    {lineName: "L1-2", x: 305, y: 340},
    {lineName: "L2", x: 335, y: 200},
    {lineName: "L3", x: 335, y: 90},
    {lineName: "L4", x: 435, y: 430},
    {lineName: "L5", x: 435, y: 340}
];

//, straight: true, reverse: true
var testGroup = [
    {people: 6, x: 525, y: 125}, // group 1-6
    {people: 6, x: 525, y: 200}, // group 7-12
    {people: 4, x: 890, y: 360}, // group 13-16
    {people: 4, x: 960, y: 440, displayed: false}, // group 17-20
    {people: 4, x: 960, y: 440}, // group 21-24
    {people: 2, x: 855, y: 235, straight: true, reverse: true},
    {people: 2, x: 855, y: 165, straight: true, reverse: true},
    {people: 2, x: 855, y: 90, straight: true, reverse: true},
    {people: 2, x: 1029, y: 360}, // group 31-32
    {people: 2, x: 890, y: 440}, // group 33-34
    {people: 1, x: 1110, y: 385, displayed: false}, // group 35
    {people: 2, x: 1110, y: 385, straight: true, reverse: true} // group 36-37
];

var babGroup = [
    {people: 6, x: 70, y: 435, lineName: "L1-1", reverse: true},
    {people: 6, x: 70, y: 360, lineName: "L1-2", reverse: true},
    {people: 6, x: 100, y: 200, lineName: "L2", reverse: true},
    {people: 6, x: 100, y: 120, lineName: "L3", reverse: true},
    {people: 6, x: 520, y: 440, lineName: "L4", reverse: true},
    {people: 6, x: 520, y: 360, lineName: "L5", reverse: true}
];

var fqcGroup = [
];

var minTestTableNo = 1;
var maxTestTableNo = 30; // no use now

var sitefloor = 3; //for test table

