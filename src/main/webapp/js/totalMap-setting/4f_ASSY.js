var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "M棟4樓",
    x: 30,
    y: 415
};

var titleGroup = [
    {lineName: "L2", x: 50, y: 120},
    {lineName: "LF", x: 870, y: 120},
    {lineName: "L3", x: 150, y: 160},
    {lineName: "L4", x: 150, y: 250},
    {lineName: "LJ", x: 870, y: 180}
];

var testGroup = [
    {people: 2, x: 470, y: 370}, // group 1-
    {people: 2, x: 550, y: 370}, // group 16-17
    {people: 1, x: 550, y: 330}, // group 12-13
    {people: 2, x: 470, y: 290}, // group 12-13
    {people: 2, x: 550, y: 290}, // group 12-13
    {people: 2, x: 630, y: 290}, // group 14-15
    {people: 2, x: 470, y: 250}, // group 18-19
    {people: 2, x: 550, y: 250}, // group 20-21
    {people: 2, x: 630, y: 250}, // group 22-23
    {people: 2, x: 470, y: 200}, // group 12-13
    {people: 2, x: 550, y: 200}, // group 14-15
    {people: 2, x: 630, y: 200}, // group 16-17
    {people: 6, x: 530, y: 110}, // group 7-11
    {people: 6, x: 530, y: 70}, // group 1-6
    {people: 6, x: 530, y: 5} // group 1-6
];

var babGroup = [
    {people: 6, x: 160, y: 120, lineName: "L2"},
    {people: 3, x: 975, y: 120, lineName: "LF"},
    {people: 6, x: 250, y: 170, lineName: "L3"},
    {people: 6, x: 250, y: 270, lineName: "L4"},
    {people: 3, x: 940, y: 180, lineName: "LJ"}
];

var fqcGroup = [
];

var minTestTableNo = 1;
var maxTestTableNo = 42; // no use now
var omitTestTableNo = [];
var nextTestTableNo;

var sitefloor = 4;