package fr.eraklys.quests;

import java.util.Map.Entry;

import com.google.gson.JsonObject;

import fr.eraklys.Eraklys;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class HandInItemTask extends QuestTask 
{
	public Item item;
	public int quantity;
	public boolean given = false;
	public String to;
	
	public boolean isDone() 
	{
		return given;
	}

	public boolean setDone() 
	{
		this.given = true;
		return given;
	}

	public void writeAdditionnalNBT(CompoundNBT compound) 
	{
		compound.putBoolean("given", this.given);
	}

	public void readNBT(CompoundNBT compound) 
	{
		this.given = compound.getBoolean("given");
	}

	public void loadFromJson(JsonObject object) 
	{		
		ResourceLocation rl = new ResourceLocation(object.get("item").getAsString());
		this.item = ForgeRegistries.ITEMS.getValue(rl);
		this.quantity = object.get("quantity").getAsInt();
		this.to = object.get("to").getAsString();
	}

	public void writeAdditionnalPacket(PacketBuffer buffer) 
	{
		buffer.writeString(item.getRegistryName().toString());
		buffer.writeInt(this.quantity);
		buffer.writeBoolean(this.given);
		buffer.writeString(this.to);
	}

	public void readAdditionnalPacket(PacketBuffer buffer) 
	{
		this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readString()));
		this.quantity = buffer.readInt();
		this.given = buffer.readBoolean();
		this.to = buffer.readString();
	}

	public void writeUpdatePacket(PacketBuffer buffer) 
	{
		buffer.writeBoolean(this.given);
	}

	public void readUpdatePacket(PacketBuffer buffer) 
	{
		this.given = buffer.readBoolean();
	}

	public ITextComponent formatedText() 
	{
		return new TranslationTextComponent("quest.giveitem", this.quantity, this.item.getName().getFormattedText(), this.to);
	}

	public QuestTask copy() 
	{
		HandInItemTask task = new HandInItemTask();
		task.setID(this.getID());
		task.item = this.item;
		task.given = this.given;
		task.to = this.to;
		
		return task;
	}

	public void updateFromPacket(QuestTask task) 
	{
		this.given = ((HandInItemTask)task).given;
	}

}
