#! octave-interpreter-name -qf
#
# load textfile
#


#graphics_toolkit("gnuplot");
#graphics_toolkit("fltk");

getenv GNUTERM;
##setenv("GNUTERM","X11")	## default
##setenv("GNUTERM","aqua")


function retval = filter_col(matrix, column, value)
	idx = ( matrix(:,column)==value );
	retval = matrix(idx,:);
endfunction

function retval = remove_filtered_col(matrix, column, value)
	m3 = filter_col(matrix, column, value);
	m(:,[column]) = []
	retval = m;
endfunction

function retval = filter_elem(matrix2d, col1val, col2val)
	m2 = filter_col(matrix2d, 1, col1val);
	m3 = filter_col(m2, 2, col2val);
	retval = m3(:,3);
endfunction


#
# Convert list of triplets to 2d matrix
#
function retval = convert_mesh(mesh_data, m, n)
	z = zeros(m, n);
	
	for i=1:m, 
		for j=1:n, 
			data = filter_elem(mesh_data, i-1, j-1);
			z(i,j) = data;
		end
	end
	
	retval = z;
endfunction


function retval = aggregate_data(raw_data, colA, colB, sumCol)
	valuesA = unique( raw_data(:, colA) );
	valuesB = unique( raw_data(:, colB) );
	
	m = rows(valuesA);
	n = rows(valuesB);
	
	z = zeros(0, 3);
	
	for ia=1:m, 
		for ib=1:n, 
			valA = valuesA(ia);
			valB = valuesA(ib);
			tmp = filter_col(raw_data, colA, valA );
			tmp = filter_col(tmp, colB, valB );
			sumVal = sum( tmp(:, sumCol) );
			sumRow = [valA, valB, sumVal];
			z = [z; sumRow];
		end
	end
	
	retval = z;
endfunction


function retval = join_vector( matrix, vector )
  ms = rows( matrix );
  if (ms == 0)
    retval = vector;
    return
  endif
  
  ret = [];
  for i = 1:rows(vector)
    X = ones(ms, 1) * vector(i,:);
    ret = [ret; matrix X];
  end
  
  retval = ret;
endfunction


function retval = fill_data(raw_data, dims, valrow)
  data_perms = [];
  colsnum = rows(dims);
  for i = 1:colsnum
    data_vec = [0 : dims(i)-1]';
    data_perms = join_vector(data_perms, data_vec);
  end
  
  data_states = raw_data(:, 1:colsnum);
  
  ret = raw_data;
  for i = 1:rows(data_perms)
    permrow = data_perms(i, :);
    
    found_rows = all(bsxfun(@eq, data_states', permrow'))';
    found_index = find( found_rows );
    
    if ( size( found_index )(1) > 0 )
	## found - skip
	continue
    endif
    
    ## row not found - add new one
    
    ret = [ret; permrow valrow];
  end
  
  retval = ret;
endfunction


##########################


filename = 'asfunc_22000.txt';

fid = fopen( filename );
txt = fgetl( fid );
txt( txt == "#" ) = " ";
fclose( fid );

data_dims = strread( txt );


raw_data=load( filename ); 
##raw_data=load('asfunc_full_left.txt'); 


state_dims = data_dims;
state_dims( rows(state_dims), : ) = [];

## state_dims

full_data = fill_data(raw_data, state_dims, [0 0] );


colA=1;
colB=2;
filtered_data = filter_col(full_data, colA, 2);
filtered_data = filter_col(filtered_data, colB, 1);
filtered_data(:,[colA, colB]) = [];		## remove filtered columns

m = state_dims(3);
n = state_dims(4);

x = 0:(m-1);
y = 0:(n-1);

mesh_left_data = [filtered_data(:,1) filtered_data(:,2) filtered_data(:,3)];
zleft = convert_mesh(mesh_left_data, m, n);

mesh_right_data = [filtered_data(:,1) filtered_data(:,2) filtered_data(:,4)];
zright = convert_mesh(mesh_right_data, m, n);

figure(1)
subplot(1, 2, 1);
meshc(x, y, zleft);
##scatter3(mesh_left_data(:,1), mesh_left_data(:,2), mesh_left_data(:,3), [], mesh_left_data(:,3)); 
xlabel("x");
ylabel("y");
title("left values ifo pole pos and speed");

subplot(1, 2, 2);
meshc(x, y, zright);
##scatter3(mesh_right_data(:,1), mesh_right_data(:,2), mesh_right_data(:,3), [], mesh_right_data(:,3)); 
xlabel("x");
ylabel("y");
title("right values ifo pole pos and speed");



##########


data_index1 = 5;
data_index2 = 6;


mesh_left_data = aggregate_data( full_data, 1, 2, data_index1 );
zleft = convert_mesh(mesh_left_data, m, n);

mesh_right_data = aggregate_data( full_data, 1, 2, data_index2 );
zright = convert_mesh(mesh_right_data, m, n);

figure(2)
subplot(1, 2, 1);
meshc(x, y, zleft);
xlabel("x");
ylabel("y");
title("aggregated left values ifo cart pos and speed");

subplot(1, 2, 2);
meshc(x, y, zright);
xlabel("x");
ylabel("y");
title("aggregated right values ifo cart pos and speed");




##########



mesh_left_data = aggregate_data( full_data, 3, 4, data_index1 );
zleft = convert_mesh(mesh_left_data, m, n);

mesh_right_data = aggregate_data( full_data, 3, 4, data_index2 );
zright = convert_mesh(mesh_right_data, m, n);

figure(3)
subplot(1, 2, 1);
meshc(x, y, zleft);
xlabel("x");
ylabel("y");
title("aggregated left values in pole pos and speed");

subplot(1, 2, 2);
meshc(x, y, zright);
xlabel("x");
ylabel("y");
title("aggregated right values in pole pos and speed");



##print -djpg image.jpg


disp("Done")
##pause()
input("Insert any key to exit", "s");
