package fr.eraklys.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Preconditions;

public class ListedMap<K, V> 
{
	private List<K> keys = new ArrayList<K>();
	private List<V> values = new ArrayList<V>();
	
	public void put(K k, V v)
	{
		this.check();
		
		ListIterator<K> ik = keys.listIterator();
		ListIterator<V> iv = values.listIterator();
		
		while(ik.hasNext() && iv.hasNext())
		{
			K key = ik.next();
			@SuppressWarnings("unused")
			V value = iv.next();
			
			if(key.equals(k))
			{
				iv.set(v);
				return;
			}
		}
		
		keys.add(k);
		values.add(v);
	}
	
	public boolean containsKey(K k)
	{
		this.check();
		
		for(K t : keys)
			if(t.equals(k))
				return true;
		
		return false;
	}
	
	public boolean containsValue(V v)
	{
		this.check();
		
		for(V t : values)
			if(t.equals(v))
				return true;
		
		return false;
	}
	
	public V get(K k)
	{
		this.check();
		
		for(int i = 0; i < keys.size() ; i++)
		{
			if(keys.get(i).equals(k))
			{
				return values.get(i);
			}
		}
		
		return null;
	}
	
	public void remove(K k)
	{
		this.check();
		
		Iterator<K> ik = keys.iterator();
		Iterator<V> iv = values.iterator();
		
		while(ik.hasNext() && iv.hasNext())
		{
			K key = ik.next();
			@SuppressWarnings("unused")
			V value = iv.next();
			
			if(key.equals(k))
			{
				ik.remove();
				iv.remove();
				return;
			}
		}
	}
	
	public List<K> listKeys()
	{
		this.check();
		return keys;
	}
	
	public List<V> listValues()
	{
		this.check();
		return values;
	}
	
	public List<Entry<K, V>> entryList()
	{
		this.check();
		List<Entry<K, V>> tr = new ArrayList<Entry<K, V>>();
		
		for(int i = 0 ; i < keys.size() ; i++)
		{
			tr.add(new Entry<K, V>(keys.get(i), values.get(i)));
		}
		return tr;
	}
	
	public int size()
	{
		this.check();
		return keys.size();
	}
	
	private void check()
	{
		Preconditions.checkState(this.keys.size() == this.values.size(), "Something went wrong with the ListedMap");
	}
	
	public static class Entry<K, V>
	{
		private K key;
		private V value;
		
		public Entry(K k, V v)
		{
			key = k;
			value = v;
		}
		
		public K getKey()
		{
			return key;
		}
		
		public V getValue()
		{
			return value;
		}
	}
}

