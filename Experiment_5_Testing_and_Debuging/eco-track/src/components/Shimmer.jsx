import React from 'react' ;

const PostShimmer = () => {
    const skeletons = [];
    for (let i = 0; i < 3; i++) {
    skeletons.push(
        <div key={i} className="bg-white rounded-2xl overflow-hidden shadow-md animate-pulse">
        <div className="h-48 bg-gray-300" />
        <div className="p-4 space-y-3">
            <div className="h-4 bg-gray-300 rounded w-3/4" />
            <div className="h-4 bg-gray-400 rounded w-1/2" />
        </div>
        </div>
    );
    }
  return (
    <div className="grid gap-8 sm:grid-cols-2 md:grid-cols-3 overflow-hidden animate-pulse py-21">
        {skeletons}
    </div>
  );
};

export default PostShimmer;
