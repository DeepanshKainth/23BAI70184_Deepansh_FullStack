import React from 'react';
import { useState } from 'react';

const SearchBar = ({setQuery}) => {
  const [input, setInput] = useState("") ;

  const handleChange = (e) => {
    setInput(e.target.value) ;
  }
  const handleKeyDown = (e) => {
    if (e.key === "Enter") setQuery(input) ;
  }
  return (
    <>
    <div className="flex items-center gap-2 mx-3 w-full max-w-md bg-white border border-gray-300 rounded-full px-4 py-2 shadow-sm focus-within:ring-2 focus-within:ring-blue-500">
      
      <input
        type="text"
        placeholder="Search..."
        className="flex-1 outline-none bg-transparent text-gray-700 placeholder-gray-400"
        value = {input}
       onChange = {handleChange}
       onKeyDown={handleKeyDown}
      />

      <button className="bg-blue-500 text-white px-4 py-1.5 rounded-full hover:bg-blue-600 transition duration-200" onClick={() => setQuery(input)
    // react does batches after the event handler finishes so if you do console log 
    // it will update later and will print the previous value
    }>
        Search
      </button>

    </div>
    <button onClick={() => {
        setQuery("");
        setInput("");
      }}
      className="
    mt-2 mb-1 rounded-full border border-blue-500 px-6 py-1.5 text-blue-500 
hover:bg-blue-500 hover:text-white transition-all duration-200 active:scale-95 ">
  All
</button>

      </>
  );
};

export default SearchBar;
