%length between points
len_O_1=25.5;
len_O_2=51;
len_O_3=50;
len_O_4=59;
len_O_5=56;
len_O_6=52.5;
len_O_7=25.5;
len_1_11=39.5;
len_1_12=40;
len_7_71=18.5;
len_7_72=41;
len_11_12=24.5;
len_12_2=26.5;
len_1_2=36;
len_2_3=49.5;
len_3_4=25.5;
len_4_5=25.5;
len_5_6=46.5;
len_6_7=32;
len_71_72=26.5;
len_6_71=24.5;

%all the angles we can now get
ang_1_O_2 = getAngleFromSSS(len_O_1,len_O_2,len_1_2);
ang_2_O_3 = getAngleFromSSS(len_O_2,len_O_3,len_2_3);
ang_3_O_4 = getAngleFromSSS(len_O_3,len_O_4,len_3_4);
ang_4_O_5 = getAngleFromSSS(len_O_4,len_O_5,len_4_5);
ang_5_O_6 = getAngleFromSSS(len_O_5,len_O_6,len_5_6);
ang_6_O_7 = getAngleFromSSS(len_O_6,len_O_7,len_6_7);
ang_72_7_71 = getAngleFromSSS(len_7_72,len_7_71,len_71_72);
ang_6_7_71 = getAngleFromSSS(len_6_7,len_7_71,len_6_71);
ang_2_1_O = getAngleFromSSS(len_1_2,len_O_1,len_O_2);
ang_6_7_O = getAngleFromSSS(len_O_7,len_6_7,len_O_6);
ang_2_1_12 = getAngleFromSSS(len_1_2,len_1_12,len_12_2);
ang_12_1_11 = getAngleFromSSS(len_1_12,len_1_11,len_11_12);

%used to get x,y coordinates for points where we can use the
%   following coordinate system:
%   O is origin. line_O_1 is positive x-axis line
r_1=len_O_1;
theta_1 = 0;
r_2=len_O_2;
theta_2 = ang_1_O_2;
r_3=len_O_3;
theta_3 = ang_2_O_3 + theta_2;
r_4=len_O_4;
theta_4 = ang_3_O_4 + theta_3;
r_5=len_O_5;
theta_5 = ang_4_O_5 + theta_4;
r_6=len_O_6;
theta_6 = ang_5_O_6 + theta_5;
r_7=len_O_7;
theta_7 = ang_6_O_7 + theta_6;

%gets x,y coordinates from above
point_0= [0 0];
point_1=getXYFromRTheta(r_1,theta_1);
point_2=getXYFromRTheta(r_2,theta_2);
point_3=getXYFromRTheta(r_3,theta_3);
point_4=getXYFromRTheta(r_4,theta_4);
point_5=getXYFromRTheta(r_5,theta_5);
point_6=getXYFromRTheta(r_6,theta_6);
point_7=getXYFromRTheta(r_7,theta_7);

points = [point_0;point_1;point_2;point_3;point_4;point_5;point_6;point_7];

%{
this is used to get x,y coordinates for points 1,2,11,12
    in the coordinate system where 1 is the origin and O_1 is the positive x-axis
%}
r_O = len_O_1;
theta_O = 0;
r_2 = len_1_2;
theta_2 = ang_2_1_O;
r_12 = len_1_12;
theta_12 = ang_2_1_12 + theta_2;
r_11 = len_1_11;
theta_11 = ang_12_1_11 + theta_12;

point_O=[getXYFromRTheta(r_O,theta_O) 1]';
point_1_new = [0 0 1]';
point_2_new = [getXYFromRTheta(r_2,theta_2) 1]';
point_12=[getXYFromRTheta(r_12,theta_12) 1]';
point_11=[getXYFromRTheta(r_11,theta_11) 1]';

%in 1's coordinate system
pointsFrom1 = [point_O point_1_new point_2_new  point_12 point_11];

%transformation to go from 1's coords to O's coords
translateToOrigin = eye(3);
translateToOrigin(1:2,3) = point_O(1:2);
reflect = eye(3);
reflect(1:2,1:2) = [-1 0; 0 1];
transform = translateToOrigin*reflect;

pointFrom1_inOspace = transform*pointsFrom1;
point_12_new = pointFrom1_inOspace(:,4);
point_11_new = pointFrom1_inOspace(:,5);

additionalPoints_1 = [point_12_new(1:2)';point_11_new(1:2)'];

%{
this is used to get x,y coordinates for points 6,7,71,72
    in the coordinate system where 7 is the origin and O_7 is the positive x-axis
%}
r_O = len_O_7;
theta_O = 0;
r_6 = len_6_7;
theta_6 = ang_6_7_O;
r_71 = len_7_71;
theta_71 = ang_6_7_71 + theta_6;
r_72 = len_7_72;
theta_72 = ang_72_7_71 + theta_71;

point_O=[getXYFromRTheta(r_O,theta_O) 1]';
point_7_new = [0 0 1]';
point_6_new = [getXYFromRTheta(r_6,theta_6) 1]';
point_71=[getXYFromRTheta(r_71,theta_71) 1]';
point_72=[getXYFromRTheta(r_72,theta_72) 1]';

%in 7's coordinate system
pointsFrom7 = [point_O point_7_new point_6_new  point_71 point_72];

%transformation to go from 7's coords to O's coords
rotAngle=theta_7-pi;
rotation = [cos(rotAngle) -sin(rotAngle) 0;sin(rotAngle) cos(rotAngle) 0; 0 0 1];
translateToOrigin = eye(3);
translateToOrigin(:,3) = ([point_7(1);point_7(2);1]);
transform = translateToOrigin*rotation;

pointFrom7_inOspace = transform*pointsFrom7;
point_71_new = pointFrom7_inOspace(:,4);
point_72_new = pointFrom7_inOspace(:,5);

additionalPoints_7 = [point_71_new(1:2)';point_72_new(1:2)'];

allPoints = [points;additionalPoints_1;additionalPoints_7];
distsReal = getPairwise(allPoints);
